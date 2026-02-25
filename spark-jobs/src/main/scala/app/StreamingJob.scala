package app

import config.SparkJobConfigLoader
import io.KafkaHdfsPostgresIO
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger
import transform.CryptoTransformations

object StreamingJob {
  def main(args: Array[String]): Unit = {
    val cfg = SparkJobConfigLoader.fromEnv()

    val spark = SparkSession.builder
      .appName("CryptoStreamingJob")
      .getOrCreate()

    val kafkaDf = KafkaHdfsPostgresIO.readKafkaStream(spark, cfg)
    val cleanDf = CryptoTransformations.parseAndClean(kafkaDf)
    val aggDf = CryptoTransformations.aggregateByMinute(cleanDf)

    val rawQuery = KafkaHdfsPostgresIO
      .writeRawToHdfs(kafkaDf, cfg)
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .start()

    val cleanQuery = KafkaHdfsPostgresIO
      .writeCleanToHdfs(cleanDf, cfg)
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .start()

    val aggQuery = aggDf.writeStream
      .trigger(Trigger.ProcessingTime("1 minute"))
      .outputMode("update")
      .foreachBatch { (batch, _) =>
        KafkaHdfsPostgresIO.writeAggToPostgres(batch, cfg)
      }
      .start()

    spark.streams.awaitAnyTermination()

    rawQuery.stop()
    cleanQuery.stop()
    aggQuery.stop()
  }
}

