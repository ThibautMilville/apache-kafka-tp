#!/usr/bin/env bash
set -eu

docker compose exec kafka kafka-topics \
  --create \
  --if-not-exists \
  --topic crypto_raw \
  --bootstrap-server kafka:9092 \
  --partitions 1 \
  --replication-factor 1
