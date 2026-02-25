package app

import config.AppConfigLoader
import http.BinanceRestClient
import kafka.JsonKafkaPublisher

object ProducerApp extends App {
  val cfg = AppConfigLoader.loadFromEnv()
  val client = new BinanceRestClient(cfg.api)
  val publisher = new JsonKafkaPublisher(cfg.kafka)

  while (true) {
    client.fetchTicker() match {
      case Right(tick) =>
        publisher.publish(tick)
      case Left(error) =>
        println(error)
    }
    Thread.sleep(cfg.api.intervalMillis)
  }
}

