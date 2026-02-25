package io

import config.SparkJobConfig
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object KafkaHdfsPostgresIO {
  def readKafkaStream(spark: SparkSession, cfg: SparkJobConfig) = {
    spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", cfg.kafka.bootstrapServers)
      .option("subscribe", cfg.kafka.topic)
      .option("startingOffsets", "latest")
      .load()
  }

  def writeRawToHdfs(df: DataFrame, cfg: SparkJobConfig) = {
    df.selectExpr("CAST(value AS STRING) as value")
      .writeStream
      .format("parquet")
      .option("path", cfg.hdfs.rawPath)
      .option("checkpointLocation", cfg.hdfs.rawPath + "_checkpoint")
      .outputMode("append")
  }

  def writeCleanToHdfs(df: DataFrame, cfg: SparkJobConfig) = {
    df.writeStream
      .format("parquet")
      .option("path", cfg.hdfs.cleanPath)
      .option("checkpointLocation", cfg.hdfs.cleanPath + "_checkpoint")
      .outputMode("append")
  }

  def writeAggToPostgres(batchDf: DataFrame, cfg: SparkJobConfig): Unit = {
    batchDf.write
      .format("jdbc")
      .mode(SaveMode.Append)
      .option("url", cfg.postgres.url)
      .option("dbtable", cfg.postgres.aggTable)
      .option("user", cfg.postgres.user)
      .option("password", cfg.postgres.password)
      .save()
  }
}

