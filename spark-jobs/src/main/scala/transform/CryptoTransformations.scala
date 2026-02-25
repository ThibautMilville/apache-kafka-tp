package transform

import org.apache.spark.sql.{DataFrame, functions => F}

object CryptoTransformations {
  def parseAndClean(df: DataFrame): DataFrame = {
    val jsonDf = df.selectExpr("CAST(value AS STRING) as json")

    val parsed = jsonDf
      .withColumn("symbol", F.get_json_object(F.col("json"), "$.symbol"))
      .withColumn("price", F.get_json_object(F.col("json"), "$.price").cast("double"))
      .withColumn("event_time", F.get_json_object(F.col("json"), "$.eventTime").cast("long"))

    parsed
      .filter(F.col("symbol").isNotNull && F.col("price").isNotNull && F.col("event_time").isNotNull)
      .withColumn("event_timestamp", F.to_timestamp(F.col("event_time") / 1000))
      .drop("json")
  }

  def aggregateByMinute(df: DataFrame): DataFrame = {
    val withWindow = df
      .withWatermark("event_timestamp", "2 minutes")
      .groupBy(
        F.window(F.col("event_timestamp"), "1 minute"),
        F.col("symbol")
      )
      .agg(
        F.first("price").alias("open_price"),
        F.max("price").alias("high_price"),
        F.min("price").alias("low_price"),
        F.last("price").alias("close_price"),
        F.count("*").alias("trade_count")
      )

    withWindow
      .withColumn("window_start", F.col("window.start"))
      .withColumn("window_end", F.col("window.end"))
      .drop("window")
  }
}

