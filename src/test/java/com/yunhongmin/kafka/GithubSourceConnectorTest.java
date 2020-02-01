package com.yunhongmin.kafka;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;

public class GithubSourceConnectorTest {
    private Map<String, String> sampleConfig() {
        Map<String, String> config = new HashMap<>();
        config.put(GithubSourceConnectorConfig.OWNER_CONFIG, "yhmin84");
        config.put(GithubSourceConnectorConfig.REPO_CONFIG, "github-source-connector");
        config.put(GithubSourceConnectorConfig.SINCE_CONFIG, "2020-01-28T01:01:01Z");
        config.put(GithubSourceConnectorConfig.BATCH_SIZE_CONFIG, "100");
        config.put(GithubSourceConnectorConfig.TOPIC_CONFIG, "github-topic");
        return config;
    }

    @Test
    public void taskConfigsAlwaysReturnOneConfig() {
        GithubSourceConnector connector = new GithubSourceConnector();
        connector.start(sampleConfig());
        Assert.assertEquals(1, connector.taskConfigs(1).size());
        Assert.assertEquals(1, connector.taskConfigs(10).size());
    }
}
