name := "spark-validation"

version := "1.0"

scalaVersion := "2.10.5"

console := {
  (runMain in Compile).toTask(" org.apache.spark.repl.Main -usejavacp").value
}

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case x if x.startsWith("META-INF") => MergeStrategy.discard // Bumf
  case x if x.endsWith(".html") => MergeStrategy.discard // More bumf
  case x if x.contains("slf4j-api") => MergeStrategy.last
  case PathList("com", "google", xs@_ *) => MergeStrategy.first
  case PathList("com", "sun", xs@_ *) => MergeStrategy.first
  case PathList("javax", "validation", xs@_ *) => MergeStrategy.last
  case x if x.contains("common-version-info.properties") => MergeStrategy.first
  case x if x.contains("LocalizedFormats_fr.properties") => MergeStrategy.last
  case x if x.contains("core-default.xml") => MergeStrategy.first
  case PathList("com", "esotericsoftware", xs@_ *) => MergeStrategy.last
  case x => MergeStrategy.last
}

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.6.1" % "provided" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy") ,
  "org.apache.spark" % "spark-sql_2.10" % "1.6.1" % "provided" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.hadoop" % "hadoop-common" % "2.6.0" % "provided" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy") excludeAll ExclusionRule(organization = "javax.servlet"),
  "org.apache.spark" % "spark-sql_2.10" % "1.6.1" % "provided" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-hive_2.10" % "1.6.1" % "provided" exclude ("org.apache.hadoop","hadoop-yarn-sver-web-proxy"),
  "org.apache.spark" % "spark-yarn_2.10" % "1.6.1" % "provided"exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy"),
  "org.apache.spark" % "spark-repl_2.10" % "1.6.1" exclude ("org.apache.hadoop","hadoop-yarn-server-web-proxy") exclude("org.scala-lang","scala-library") exclude("org.apache.spark","spark-sql_2.10") ,

  "org.gbif.validator" % "validator-core" % "0.1-SNAPSHOT" exclude("org.slf4j","slf4j-log4j12") exclude("org.slf4j","log4j-over-slf4j") exclude("com.typesafe.akka","akka-actor_2.11") exclude("org.scala-lang","scala-library"),
  "org.gbif" % "gbif-api" % "0.48-SNAPSHOT",
  "org.gbif.registry" % "registry-ws-client" % "2.60-SNAPSHOT",
  "org.gbif" % "dwc-api" % "1.17-SNAPSHOT",
   "com.sun.jersey" % "jersey-servlet" % "1.19"

)
