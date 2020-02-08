package com.yunhongmin.kafka;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.yunhongmin.kafka.models.Issue;
import org.json.JSONObject;
import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.yunhongmin.kafka.GithubSourceConnectorConfig.*;

public class GithubSourceTaskTest {

    GithubSourceTask gitHubSourceTask = new GithubSourceTask();
    private Integer batchSize = 10;

    private Map<String, String> initialConfig() {
        Map<String, String> baseProps = new HashMap<>();
        baseProps.put(OWNER_CONFIG, "apache");
        baseProps.put(REPO_CONFIG, "kafka");
        baseProps.put(SINCE_CONFIG, "2017-01-01T00:00:00Z");
        baseProps.put(BATCH_SIZE_CONFIG, batchSize.toString());
        baseProps.put(TOPIC_CONFIG, "github-issues");
        return baseProps;
    }


    @Test
    public void test() throws UnirestException {
        gitHubSourceTask.config = new GithubSourceConnectorConfig(initialConfig());
        gitHubSourceTask.nextPageToVisit = 1;
        gitHubSourceTask.nextQuerySince = Instant.parse("2017-01-01T00:00:00Z");
        gitHubSourceTask.githubAPIHttpClient = new GithubAPIHttpClient(gitHubSourceTask.config);
        String url = gitHubSourceTask.githubAPIHttpClient.constructUrl(gitHubSourceTask.nextPageToVisit, gitHubSourceTask.nextQuerySince);
        System.out.println(url);
        HttpResponse<JsonNode> httpResponse = gitHubSourceTask.githubAPIHttpClient
                .callIssuesAPI(gitHubSourceTask.nextPageToVisit, gitHubSourceTask.nextQuerySince);
        if (httpResponse.getStatus() != 403) {
            assert (httpResponse.getStatus() == 200);
            Set<String> headers = httpResponse.getHeaders().keySet();
            assert (headers.contains("ETag"));
            assert (headers.contains("X-RateLimit-Limit"));
            assert (headers.contains("X-RateLimit-Remaining"));
            assert (headers.contains("X-RateLimit-Reset"));
            assert (httpResponse.getBody().getArray().length() == 10);
            JSONObject jsonObject = (JSONObject) httpResponse.getBody().getArray().get(0);
            Issue issue = Issue.fromJSON(jsonObject);
            assert (issue != null);
            gitHubSourceTask.sourcePartition();
            gitHubSourceTask.sourceOffset(issue.getUpdatedAt());
            assert (issue.getNumber() == 2072);
        }
    }
}