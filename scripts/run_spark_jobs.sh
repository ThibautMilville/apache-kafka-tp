#!/usr/bin/env bash
set -eu

docker compose exec spark-master bash -c '
  mkdir -p /tmp/.ivy2 &&
  /opt/spark/bin/spark-submit \
    --master spark://spark-master:7077 \
    --class app.StreamingJob \
    --conf spark.jars.ivy=/tmp/.ivy2 \
    --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.1,org.postgresql:postgresql:42.7.3 \
    /opt/spark/jobs/target/scala-2.12/crypto-spark-jobs_2.12-0.1.0-SNAPSHOT.jar
'
