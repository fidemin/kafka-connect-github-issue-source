#!/usr/bin/env bash
# jar file is created by using IntelliJ artifact
export CLASSPATH=$(pwd)/out/artifacts/kafka_connect_github_source_jar/kafka-connect-github-source.jar
connect-standalone config/worker.properties config/github-source-connector.properties
