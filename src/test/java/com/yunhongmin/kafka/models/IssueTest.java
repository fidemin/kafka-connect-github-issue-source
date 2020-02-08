package com.yunhongmin.kafka.models;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IssueTest {
    String issueStr = "{\n" +
            "  \"url\": \"https://api.github.com/repos/apache/kafka/issues/2800\",\n" +
            "  \"repository_url\": \"https://api.github.com/repos/apache/kafka\",\n" +
            "  \"labels_url\": \"https://api.github.com/repos/apache/kafka/issues/2800/labels{/name}\",\n" +
            "  \"comments_url\": \"https://api.github.com/repos/apache/kafka/issues/2800/comments\",\n" +
            "  \"events_url\": \"https://api.github.com/repos/apache/kafka/issues/2800/events\",\n" +
            "  \"html_url\": \"https://github.com/apache/kafka/pull/2800\",\n" +
            "  \"id\": 219155037,\n" +
            "  \"number\": 2800,\n" +
            "  \"title\": \"added interface to allow producers to create a ProducerRecord without…\",\n" +
            "  \"user\": {\n" +
            "    \"login\": \"yhmin84\",\n" +
            "    \"id\": 123456,\n" +
            "    \"avatar_url\": \"https://avatars3.githubusercontent.com/u/20851561?v=3\",\n" +
            "    \"gravatar_id\": \"\",\n" +
            "    \"url\": \"https://api.github.com/users/yhmin84\",\n" +
            "    \"html_url\": \"https://github.com/yhmin84\",\n" +
            "    \"followers_url\": \"https://api.github.com/users/yhmin84/followers\",\n" +
            "    \"following_url\": \"https://api.github.com/users/yhmin84/following{/other_user}\",\n" +
            "    \"gists_url\": \"https://api.github.com/users/yhmin84/gists{/gist_id}\",\n" +
            "    \"starred_url\": \"https://api.github.com/users/yhmin84/starred{/owner}{/repo}\",\n" +
            "    \"subscriptions_url\": \"https://api.github.com/users/yhmin84/subscriptions\",\n" +
            "    \"organizations_url\": \"https://api.github.com/users/yhmin84/orgs\",\n" +
            "    \"repos_url\": \"https://api.github.com/users/yhmin84/repos\",\n" +
            "    \"events_url\": \"https://api.github.com/users/yhmin84/events{/privacy}\",\n" +
            "    \"received_events_url\": \"https://api.github.com/users/yhmin84/received_events\",\n" +
            "    \"type\": \"User\",\n" +
            "    \"site_admin\": false\n" +
            "  },\n" +
            "  \"labels\": [],\n" +
            "  \"state\": \"closed\",\n" +
            "  \"locked\": false,\n" +
            "  \"assignee\": null,\n" +
            "  \"assignees\": [],\n" +
            "  \"milestone\": null,\n" +
            "  \"comments\": 12,\n" +
            "  \"created_at\": \"2017-04-04T06:47:09Z\",\n" +
            "  \"updated_at\": \"2017-04-19T22:36:21Z\",\n" +
            "  \"closed_at\": \"2017-04-19T22:36:21Z\",\n" +
            "  \"pull_request\": {\n" +
            "    \"url\": \"https://api.github.com/repos/apache/kafka/pulls/2800\",\n" +
            "    \"html_url\": \"https://github.com/apache/kafka/pull/2800\",\n" +
            "    \"diff_url\": \"https://github.com/apache/kafka/pull/2800.diff\",\n" +
            "    \"patch_url\": \"https://github.com/apache/kafka/pull/2800.patch\"\n" +
            "  },\n" +
            "  \"body\": \"… specifying a partition, making it more obvious that the parameter partition can be null\"\n" +
            "}";

    JSONObject issueJson = new JSONObject(issueStr);

    @Test
    public void issueObjectFromJson() {
        Issue issue = Issue.fromJSON(issueJson);
        assertEquals("2017-04-04T06:47:09Z", issue.getCreatedAt().toString());
        assertEquals("2017-04-19T22:36:21Z", issue.getUpdatedAt().toString());
        assertEquals((long) 2800, issue.getNumber().longValue());
        assertEquals("https://api.github.com/repos/apache/kafka/issues/2800", issue.getUrl());
        assertEquals("closed", issue.getState());
        assertEquals(
                "added interface to allow producers to create a ProducerRecord without…",
                issue.getTitle());
        User user = issue.getUser();
        assertEquals((long) 123456, user.getId().longValue());
        assertEquals("yhmin84", user.getLogin());
        assertEquals("https://api.github.com/users/yhmin84", user.getUrl());

        PullRequest pullRequest = issue.getPullRequest();
        assertEquals("https://api.github.com/repos/apache/kafka/pulls/2800", pullRequest.getUrl());
        assertEquals("https://github.com/apache/kafka/pull/2800", pullRequest.getHtmlUrl());
    }

}
