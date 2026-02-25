package config

final case class KafkaSourceConfig(
  bootstrapServers: String,
  topic: String
)

final case class HdfsConfig(
  rawPath: String,
  cleanPath: String
)

final case class PostgresConfig(
  url: String,
  user: String,
  password: String,
  cleanTable: String,
  aggTable: String
)

final case class SparkJobConfig(
  kafka: KafkaSourceConfig,
  hdfs: HdfsConfig,
  postgres: PostgresConfig
)

object SparkJobConfigLoader {
  def fromEnv(): SparkJobConfig = {
    val kafkaBootstrap = sys.env.getOrElse("KAFKA_BOOTSTRAP_SERVERS", "kafka:9092")
    val kafkaTopic = sys.env.getOrElse("KAFKA_TOPIC", "crypto_raw")

    val rawPath = sys.env.getOrElse("HDFS_RAW_PATH", "hdfs://namenode:8020/datalake/raw/crypto")
    val cleanPath = sys.env.getOrElse("HDFS_CLEAN_PATH", "hdfs://namenode:8020/datalake/clean/crypto")

    val pgUrl = sys.env.getOrElse("POSTGRES_JDBC_URL", "jdbc:postgresql://postgres:5432/crypto")
    val pgUser = sys.env.getOrElse("POSTGRES_USER", "crypto")
    val pgPassword = sys.env.getOrElse("POSTGRES_PASSWORD", "crypto")
    val cleanTable = sys.env.getOrElse("POSTGRES_CLEAN_TABLE", "crypto.crypto_prices_clean")
    val aggTable = sys.env.getOrElse("POSTGRES_AGG_TABLE", "crypto.crypto_prices_agg_1min")

    SparkJobConfig(
      kafka = KafkaSourceConfig(kafkaBootstrap, kafkaTopic),
      hdfs = HdfsConfig(rawPath, cleanPath),
      postgres = PostgresConfig(pgUrl, pgUser, pgPassword, cleanTable, aggTable)
    )
  }
}

