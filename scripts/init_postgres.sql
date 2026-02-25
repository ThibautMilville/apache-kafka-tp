CREATE DATABASE crypto;

\connect crypto;

CREATE SCHEMA IF NOT EXISTS crypto;

CREATE TABLE IF NOT EXISTS crypto.crypto_prices_clean (
  event_time BIGINT NOT NULL,
  event_timestamp TIMESTAMPTZ NOT NULL,
  symbol TEXT NOT NULL,
  price DOUBLE PRECISION NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_crypto_prices_clean_symbol_time
  ON crypto.crypto_prices_clean (symbol, event_timestamp);

CREATE TABLE IF NOT EXISTS crypto.crypto_prices_agg_1min (
  window_start TIMESTAMPTZ NOT NULL,
  window_end TIMESTAMPTZ NOT NULL,
  symbol TEXT NOT NULL,
  open_price DOUBLE PRECISION NOT NULL,
  high_price DOUBLE PRECISION NOT NULL,
  low_price DOUBLE PRECISION NOT NULL,
  close_price DOUBLE PRECISION NOT NULL,
  trade_count BIGINT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_crypto_prices_agg_symbol_time
  ON crypto.crypto_prices_agg_1min (symbol, window_start);

