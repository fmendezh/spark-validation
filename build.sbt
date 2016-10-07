name := "spark-validation"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.0.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-sql_2.11" % "2.0.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.hadoop" % "hadoop-common" % "2.7.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-sql_2.11" % "2.0.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-hive_2.11" % "2.0.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-yarn_2.11" % "2.0.0" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),

  "org.gbif.validator" % "validator-core" % "0.1-SNAPSHOT" exclude("org.slf4j","slf4j-log4j12") exclude("org.slf4j","log4j-over-slf4j"),
  "org.gbif" % "gbif-api" % "0.48-SNAPSHOT",
  "org.gbif.registry" % "registry-ws-client" % "2.60-SNAPSHOT",
  "org.gbif" % "dwc-api" % "1.17-SNAPSHOT",

  "com.esotericsoftware.kryo" % "kryo" % "2.16"

)
