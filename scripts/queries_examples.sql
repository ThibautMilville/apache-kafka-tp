SELECT
  symbol,
  date_trunc('minute', event_timestamp) AS minute_bucket,
  AVG(price) AS avg_price
FROM crypto.crypto_prices_clean
GROUP BY symbol, date_trunc('minute', event_timestamp)
ORDER BY minute_bucket DESC
LIMIT 100;

SELECT
  symbol,
  window_start,
  window_end,
  open_price,
  high_price,
  low_price,
  close_price,
  trade_count
FROM crypto.crypto_prices_agg_1min
ORDER BY window_start DESC
LIMIT 100;

