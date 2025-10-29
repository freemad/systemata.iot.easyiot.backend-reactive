#!/usr/bin/sh

curl -X POST http://<connect-host>:8083/connectors \
  -H "Content-Type: application/json" \
  -d @timescale-sink-config.json