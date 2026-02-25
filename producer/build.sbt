ThisBuild / scalaVersion := "2.13.14"

name := "crypto-producer"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.7.0",
  "com.softwaremill.sttp.client3" %% "core" % "3.9.5",
  "io.circe" %% "circe-core" % "0.14.9",
  "io.circe" %% "circe-generic" % "0.14.9",
  "io.circe" %% "circe-parser" % "0.14.9"
)

