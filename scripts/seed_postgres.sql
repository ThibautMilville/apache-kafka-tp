INSERT INTO crypto.crypto_prices_clean (event_time, event_timestamp, symbol, price)
VALUES (EXTRACT(EPOCH FROM NOW())::BIGINT, NOW(), 'BTCUSDT', 50000.0);

INSERT INTO crypto.crypto_prices_agg_1min (
  window_start,
  window_end,
  symbol,
  open_price,
  high_price,
  low_price,
  close_price,
  trade_count
)
VALUES (
  NOW() - INTERVAL '1 minute',
  NOW(),
  'BTCUSDT',
  50000.0,
  50500.0,
  49500.0,
  50200.0,
  100
);

