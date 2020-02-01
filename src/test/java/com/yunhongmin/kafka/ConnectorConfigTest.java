package com.yunhongmin.kafka;

import org.apache.kafka.common.config.ConfigDef;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ConnectorConfigTest {
    private ConfigDef configDef = ConnectorConfig.getConfDef();

    private Map<String, String> sampleConfig() {
        Map<String, String> config = new HashMap<>();
        config.put(ConnectorConfig.OWNER_CONFIG, "yhmin84");
        config.put(ConnectorConfig.REPO_CONFIG, "foo");
        config.put(ConnectorConfig.SINCE_CONFIG, "2020-01-28T01:01:01Z");
        config.put(ConnectorConfig.BATCH_SIZE_CONFIG, "100");
        config.put(ConnectorConfig.TOPIC_CONFIG, "github-topic");
        return config;
    }


    @Test
    public void configValid() {
        assert (configDef.validate(sampleConfig())
                .stream()
                .allMatch(configValue -> configValue.errorMessages().size() == 0));
    }
}
