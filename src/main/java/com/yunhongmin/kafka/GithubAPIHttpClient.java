package com.yunhongmin.kafka;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.apache.kafka.connect.errors.ConnectException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * used to send and get result from Github issue HTTP GET API
 */
public class GithubAPIHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(GithubAPIHttpClient.class);

    private Integer xRateLimit = 9999;
    private Integer xRateRemaining = 9999;
    private long xRateReset = Instant.MAX.getEpochSecond();

    GithubSourceConnectorConfig config;

    public GithubAPIHttpClient(GithubSourceConnectorConfig config) {this.config = config;}

    protected JSONArray getIssues(Integer page, Instant since) throws InterruptedException {
        try {
            HttpResponse<JsonNode> jsonResponse = sendIssuesAPI(page, since);

            Headers headers = jsonResponse.getHeaders();
            xRateLimit = Integer.valueOf(headers.getFirst("X-RateLimit-Limit"));
            xRateRemaining = Integer.valueOf(headers.getFirst("X-RateLimit-Remaining"));
            xRateReset = Integer.valueOf(headers.getFirst("X-RateLimit-Reset"));

            switch (jsonResponse.getStatus()) {
                case 200:
                    return jsonResponse.getBody().getArray();
                case 401:
                    throw new ConnectException("Bad Github credentials provided");
                case 403:
                    // too many request issues
                    logger.info(jsonResponse.getBody().getObject().getString("message"));
                    logger.info(String.format("Your rate limit is %s", xRateLimit));
                    logger.info(String.format("Your rate limit remaining is %s", xRateRemaining));
                    logger.info(String.format("Your limit will be reset at %s",
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(xRateReset), ZoneOffset.systemDefault())));
                    long sleepTime = xRateReset = Instant.now().getEpochSecond();
                    logger.info(String.format("Sleeping for %s secondes", sleepTime));
                    Thread.sleep(1000*sleepTime);
                    return getIssues(page, since);
                default:
                    logger.error(constructUrl(page, since));
                    logger.error(String.valueOf(jsonResponse.getStatus()));
                    logger.error(jsonResponse.getBody().toString());
                    logger.error(jsonResponse.getHeaders().toString());
                    logger.error("Unknown error: Sleeping 5 seconds " +
                            "before re-trying");
                    Thread.sleep(5000L);
                    return getIssues(page, since);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            Thread.sleep(5000L);
            return new JSONArray();
        }
    }

    protected HttpResponse<JsonNode> sendIssuesAPI(Integer page, Instant since) throws UnirestException {
        GetRequest request = Unirest.get(constructUrl(page, since));
        if (!config.getAuthUsername().isEmpty() && !config.getAuthUsername().isEmpty()) {
            request.basicAuth(config.getAuthUsername(), config.getAuthPassword());
        }
        return request.asJson();
    }

    protected String constructUrl(Integer page, Instant since) {
        return String.format(
                "http://api.github.com/repos/%s/%s/issues?page=%s&per_page=%s&since=%s&state=all&direction=asc&sort=updated",
                config.getOwnerConfig(),
                config.getRepoConfig(),
                page,
                config.getBatchSize(),
                since.toString()
        );
    }

    public void sleep() throws InterruptedException {
        // Split total sleep time evenly by xRateRemaining until reaching xRateReset time.
        long sleepTime = (long) Math.ceil(
                (double) ((xRateReset - Instant.now().getEpochSecond()) / xRateRemaining)
        );
        logger.debug(String.format("Sleeping for %s seconds", sleepTime ));
        // There is small possibility sleepTime can be less than zero.
        if (sleepTime > 0) {
            Thread.sleep(1000*sleepTime);
        }
    }

    public void sleepIfNeeds() throws InterruptedException {
        if (xRateRemaining > 0 && xRateRemaining <= 10) {
            logger.info(String.format("We are approaching the limit. only %s left", xRateRemaining));
            sleep();
        }
    }
}
