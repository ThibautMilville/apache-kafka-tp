package http

import config.ApiConfig
import sttp.client3._
import io.circe._
import io.circe.parser._

final case class TickerTick(
  symbol: String,
  price: BigDecimal,
  eventTime: Long
)

trait CryptoClient {
  def fetchTicker(): Either[String, TickerTick]
}

final class BinanceRestClient(apiConfig: ApiConfig) extends CryptoClient {
  private val backend = HttpURLConnectionBackend()

  def fetchTicker(): Either[String, TickerTick] = {
    val request = basicRequest
      .get(uri"${apiConfig.baseUrl}/api/v3/ticker/price?symbol=${apiConfig.symbol}")
      .response(asString)

    request.send(backend).body.left.map(_.toString).flatMap { body =>
      parse(body).left.map(_.toString).flatMap { json =>
        for {
          symbol <- json.hcursor.get[String]("symbol")
          priceStr <- json.hcursor.get[String]("price")
          price <- Right(BigDecimal(priceStr))
        } yield TickerTick(
          symbol = symbol,
          price = price,
          eventTime = System.currentTimeMillis()
        )
      }.left.map(_.toString)
    }
  }
}

