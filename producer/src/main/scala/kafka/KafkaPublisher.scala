package kafka

import config.KafkaConfig
import http.TickerTick
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import java.util.Properties
import io.circe.syntax._
import io.circe.generic.auto._

trait KafkaPublisher {
  def publish(tick: TickerTick): Unit
}

final class JsonKafkaPublisher(cfg: KafkaConfig) extends KafkaPublisher {
  private val props = new Properties()
  props.put("bootstrap.servers", cfg.bootstrapServers)
  props.put("client.id", cfg.clientId)
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  private val producer = new KafkaProducer[String, String](props)

  def publish(tick: TickerTick): Unit = {
    val json = tick.asJson.noSpaces
    val record = new ProducerRecord[String, String](cfg.topic, tick.symbol, json)
    producer.send(record)
    producer.flush()
  }
}

