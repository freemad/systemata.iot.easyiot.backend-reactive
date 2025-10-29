#!/usr/bin/sh

curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" -d '{
  "name": "influxdb-sink",
  "config": {
    "connector.class": "com.datamountaineer.streamreactor.connect.influx.InfluxSinkConnector",
    "tasks.max": "1",
    "topics": "telemetry.device",
    "connect.influx.url": "http://influxdb:8086",
    "connect.influx.db": "eiot",
    "connect.influx.username": "admin",
    "connect.influx.password": "password",
    "connect.influx.kcql": "INSERT INTO telemetry SELECT * FROM telemetry.device",
    "connect.progress.enabled": "true"
  }
}'