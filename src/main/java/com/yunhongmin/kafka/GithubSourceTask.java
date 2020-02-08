package com.yunhongmin.kafka;

import com.yunhongmin.kafka.models.Issue;
import com.yunhongmin.kafka.models.PullRequest;
import com.yunhongmin.kafka.models.User;
import com.yunhongmin.kafka.schemas.IssueSchema;
import com.yunhongmin.kafka.schemas.KeySchema;
import com.yunhongmin.kafka.schemas.PullRequestSchema;
import com.yunhongmin.kafka.schemas.UserSchema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GithubSourceTask extends SourceTask {
    private static final Logger logger = LoggerFactory.getLogger(GithubSourceTask.class);
    private String NEXT_PAGE_FIELD = "next_page";
    private String SINCE_FIELD = "since";

    protected GithubSourceConnectorConfig config;
    protected GithubAPIHttpClient githubAPIHttpClient;

    protected Instant lastUpdatedAt;
    protected Integer nextPageToVisit = 1;
    protected Instant nextQuerySince;
    protected Integer lastIssueNumber = -1;

    @Override
    public void start(Map<String, String> map) {
        config = new GithubSourceConnectorConfig(map);
        initializeLastVariables();
        githubAPIHttpClient = new GithubAPIHttpClient(config);
    }

    private void initializeLastVariables() {
        Map<String, Object> lastSourceOffset;
        lastSourceOffset = context.offsetStorageReader().offset(sourcePartition());

        if (lastSourceOffset == null) {
            nextQuerySince = config.getSince();
        } else {
            Object updatedAt = lastSourceOffset.get(IssueSchema.UPDATED_AT_FIELD);
            Object issueNumber = lastSourceOffset.get(IssueSchema.NUMBER_FIELD);
            Object nextPage = lastSourceOffset.get(NEXT_PAGE_FIELD);
            if (updatedAt instanceof String) {
                nextQuerySince = Instant.parse((String) updatedAt);
            }

            if (issueNumber instanceof String) {
                lastIssueNumber = Integer.valueOf((String) issueNumber);
            }

            if (nextPage instanceof String) {
                nextPageToVisit = Integer.valueOf((String) nextPage);
            }
        }
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        githubAPIHttpClient.sleepIfNeeds();

        final ArrayList<SourceRecord> records = new ArrayList<>();
        JSONArray issues = githubAPIHttpClient.getIssues(nextPageToVisit, nextQuerySince);

        int i = 0;
        for (Object obj: issues) {
            Issue issue = Issue.fromJSON((JSONObject) obj);
            SourceRecord sourceRecord = generateSourceRecord(issue);
            records.add(sourceRecord);
            i += 1;
            lastUpdatedAt = issue.getUpdatedAt();
        }

        logger.info(String.format("Fetched %s records", i));

        if (i==config.getBatchSize()) {
            nextPageToVisit += 1;
        } else {
            nextQuerySince = lastUpdatedAt.plusSeconds(1);
            nextPageToVisit = 1;
            githubAPIHttpClient.sleep();
        }

        return records;
    }

    @Override
    public void stop() {

    }

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    protected Map<String, String> sourcePartition() {
        Map<String, String> map = new HashMap<>();
        map.put(KeySchema.OWNER_FIELD, config.getOwnerConfig());
        map.put(KeySchema.REPOSITORY_FIELD, config.getRepoConfig());
        map.put(SINCE_FIELD, config.getSince().toString());
        return map;
    }

    protected Map<String, String> sourceOffset(Instant updatedAt) {
        Map<String, String> map = new HashMap<>();
        Instant maxDatetime = updatedAt.compareTo(nextQuerySince) > 0 ? updatedAt: nextQuerySince;
        map.put(IssueSchema.UPDATED_AT_FIELD, maxDatetime.toString());
        map.put(NEXT_PAGE_FIELD, nextPageToVisit.toString());
        map.put(IssueSchema.NUMBER_FIELD, lastIssueNumber.toString());
        return map;
    }

    private SourceRecord generateSourceRecord(Issue issue) {
        return new SourceRecord(
                sourcePartition(),
                sourceOffset(issue.getUpdatedAt()),
                config.getTopic(),
                null, // partition will be inferred by the kafka
                KeySchema.SCHEMA,
                buildRecordKey(issue),
                IssueSchema.SCHEMA,
                buildRecordValue(issue),
                issue.getUpdatedAt().toEpochMilli()
        );
    }

    private Struct buildRecordKey(Issue issue) {
        Struct key = new Struct(KeySchema.SCHEMA)
                .put(KeySchema.OWNER_FIELD, config.getOwnerConfig())
                .put(KeySchema.REPOSITORY_FIELD, config.getRepoConfig())
                .put(IssueSchema.NUMBER_FIELD, issue.getNumber());
        return key;
    }

    private Struct buildRecordValue(Issue issue) {
        Struct issueStruct = new Struct(IssueSchema.SCHEMA)
                .put(IssueSchema.NUMBER_FIELD, issue.getNumber())
                .put(IssueSchema.TITLE_FIELD, issue.getTitle())
                .put(IssueSchema.STATE_FIELD, issue.getState())
                .put(IssueSchema.URL_FIELD, issue.getUrl())
                .put(IssueSchema.CREATED_AT_FIELD, issue.getCreatedAt().toEpochMilli())
                .put(IssueSchema.UPDATED_AT_FIELD, issue.getUpdatedAt().toEpochMilli());

        User user = issue.getUser();
        Struct userStruct = new Struct(UserSchema.SCHEMA)
                .put(UserSchema.USER_ID_FIELD, user.getId())
                .put(UserSchema.USER_LOGIN_FIELD, user.getLogin())
                .put(UserSchema.USER_URL_FIELD, user.getUrl());
        issueStruct.put(IssueSchema.USER_FIELD, userStruct);

        PullRequest pr = issue.getPullRequest();
        if (pr != null) {
            Struct prStruct = new Struct(PullRequestSchema.SCHEMA)
                    .put(PullRequestSchema.PR_URL_FIELD, pr.getUrl())
                    .put(PullRequestSchema.PR_HTML_URL_FIELD, pr.getHtmlUrl());
            issueStruct.put(IssueSchema.PR_FIELD, prStruct);
        }
        return issueStruct;
    }
}
