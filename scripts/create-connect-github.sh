#!/usr/bin/env bash

curl -X POST \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d '{
  "name": "github-source-connector-demo",
  "config": {
    "connector.class": "com.yunhongmin.kafka.GithubSourceConnector",
    "tasks.max": 1,
	"topic": "github-issues",
	"github.owner": "kubernetes",
	"github.repo": "kubernetes",
	"since.timestamp": "2020-02-07T00:00:00Z"
  }
}' http://127.0.0.1:8083/connectors

