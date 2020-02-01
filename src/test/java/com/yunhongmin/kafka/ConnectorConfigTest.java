package com.yunhongmin.kafka;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigValue;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ConnectorConfigTest {
    private ConfigDef configDef = ConnectorConfig.getConfDef();

    private Map<String, String> sampleConfig() {
        Map<String, String> config = new HashMap<>();
        config.put(ConnectorConfig.OWNER_CONFIG, "yhmin84");
        config.put(ConnectorConfig.REPO_CONFIG, "github-source-connector");
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

    @Test
    public void batchSizeNotValid() {
        Map<String, String> config = sampleConfig();
        config.put(ConnectorConfig.BATCH_SIZE_CONFIG, "-1");
        ConfigValue configValue = configDef.validateAll(config).get(ConnectorConfig.BATCH_SIZE_CONFIG);
        assert (configValue.errorMessages().size() > 0);

        config = sampleConfig();
        config.put(ConnectorConfig.BATCH_SIZE_CONFIG, "101");
        configValue = configDef.validateAll(config).get(ConnectorConfig.BATCH_SIZE_CONFIG);
        assert (configValue.errorMessages().size() > 0);
    }

    @Test
    public void sinceNotValid() {
        Map<String, String> config = sampleConfig();
        config.put(ConnectorConfig.SINCE_CONFIG, "not a date");
        ConfigValue configValue = configDef.validateAll(config).get(ConnectorConfig.SINCE_CONFIG);
        assert (configValue.errorMessages().size() > 0);
    }
}
