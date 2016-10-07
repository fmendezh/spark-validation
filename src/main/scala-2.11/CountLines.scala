/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{SparkSession, Row}
import scala.collection.JavaConverters._
import org.gbif.dwc.terms.Term
import org.gbif.occurrence.validation.evaluator.OccurrenceEvaluatorFactory
import org.gbif.occurrence.validation.util.TempTermsUtils


object CountLines {
  def main(args: Array[String]) {
    val t0 = System.currentTimeMillis();
    val dataFile = "/Users/fmendez/dev/git/gbif/gbif-data-validator/validator-core/src/test/resources/0008759-160822134323880.csvar" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local[20]")
    val sc = new SparkContext(conf)
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate();
    val data = sc.textFile(dataFile, 20).cache()

    val header = data.first()
    val rawHeader = header.split("\t")
    val terms: Array[Term] = TempTermsUtils.buildTermMapping(header.split("\t"))

    //This creates a schema from the header
    val schema = StructType(rawHeader.map(fieldName ⇒ StructField(fieldName, StringType, true)))

    // this is to ensure that each row has the same number as columns as reported in the header
    //RDD[Row] is the data type expected by the session.createDataFrame
    val rowData: RDD[Row] = data.zipWithIndex().filter(line => line._2 != 0)
      .map(line => Row.fromSeq(line._1.split("\t").padTo(rawHeader.length,"")))

    val ds = session.createDataFrame(rowData,schema)

    //Creates the view
    val occDs = ds.createTempView("occ")

    //runs a sql statement
    val idCounts = session.sql("select count(distinct occurrenceid) from occ").collect()

    //This is a bit of duplication: runs all the processing
    val results = data.zipWithIndex().filter(line => line._2 != 0)
                       .map(line => (line._2,(terms.zip(line._1.split("\t"))).toMap))
                       .mapPartitions( partition => {
                         val occEvaluator  = new OccurrenceEvaluatorFactory("http://api.gbif.org/v1/").create(rawHeader)
                         val newPartition = partition.map(record => {
                           occEvaluator.process(record._1, record._2.asJava)
                         }).toList // consumes the iterator, thus calls readMatchingFromDB
                         newPartition.iterator
                       }).collect()
    val t1 = System.currentTimeMillis();


    println("Elapsed time: " + (t1 - t0)/1000 + "s")

  }
}

