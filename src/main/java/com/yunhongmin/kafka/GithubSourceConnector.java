package com.yunhongmin.kafka;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GithubSourceConnector extends SourceConnector {
    GithubSourceConnectorConfig config;

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> map) {
        config = new GithubSourceConnectorConfig(map);
    }

    @Override
    public Class<? extends Task> taskClass() {
        // TODO: need to add task class after creating the class
        return null;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int i) {
        // Regardless of the value of i, returns only 1 task config;
        ArrayList<Map<String, String>> configs = new ArrayList<>(1);
        configs.add(config.originalsStrings());
        return configs;
    }

    @Override
    public void stop() {
        // Do necessary to stop your connector
        // Nothing to do for this connector
    }

    @Override
    public ConfigDef config() {
        return GithubSourceConnectorConfig.confDef();
    }
}
