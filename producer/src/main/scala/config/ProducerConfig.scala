package config

final case class ApiConfig(
  baseUrl: String,
  symbol: String,
  intervalMillis: Long
)

final case class KafkaConfig(
  bootstrapServers: String,
  topic: String,
  clientId: String
)

final case class AppConfig(
  api: ApiConfig,
  kafka: KafkaConfig
)

object AppConfigLoader {
  def loadFromEnv(): AppConfig = {
    val apiBaseUrl = sys.env.getOrElse("CRYPTO_API_URL", "https://api.binance.com")
    val symbol = sys.env.getOrElse("CRYPTO_SYMBOL", "BTCUSDT")
    val intervalMillis = sys.env.getOrElse("CRYPTO_POLL_INTERVAL_MS", "5000").toLong
    val kafkaBootstrap = sys.env.getOrElse("KAFKA_BOOTSTRAP_SERVERS", "kafka:9092")
    val kafkaTopic = sys.env.getOrElse("KAFKA_TOPIC", "crypto_raw")
    val clientId = sys.env.getOrElse("KAFKA_CLIENT_ID", "crypto-producer")

    AppConfig(
      api = ApiConfig(apiBaseUrl, symbol, intervalMillis),
      kafka = KafkaConfig(kafkaBootstrap, kafkaTopic, clientId)
    )
  }
}

