ThisBuild / scalaVersion := "2.12.18"

name := "crypto-spark-jobs"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "3.5.1" % "provided",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.5.1" % "provided",
  "org.postgresql" % "postgresql" % "42.7.3"
)

