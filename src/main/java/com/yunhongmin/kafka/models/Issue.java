package com.yunhongmin.kafka.models;

import com.yunhongmin.kafka.schemas.IssueSchema;
import org.json.JSONObject;

public class Issue {
    private String createdAt;
    private String updatedAt;
    private Integer number;
    private String url;
    private String title;
    private String state;
    private User user;
    private PullRequest pullRequest;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createAt) {
        this.createdAt = createAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PullRequest getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
    }

    public static Issue fromJSON(JSONObject json) {
        Issue issue = new Issue();
        issue.setCreatedAt(json.getString(IssueSchema.CREATED_AT_FIELD));
        issue.setUpdatedAt(json.getString(IssueSchema.UPDATED_AT_FIELD));
        issue.setNumber(json.getInt(IssueSchema.NUMBER_FIELD));
        issue.setUrl(json.getString(IssueSchema.URL_FIELD));
        issue.setState(json.getString(IssueSchema.STATE_FIELD));
        issue.setTitle(json.getString(IssueSchema.TITLE_FIELD));
        issue.setUser(User.fromJson(json.getJSONObject(IssueSchema.USER_FIELD)));

        if (json.has(IssueSchema.PR_FIELD)) {
            issue.setPullRequest(PullRequest.fromJson(json.getJSONObject(IssueSchema.PR_FIELD)));
        }

        return issue;
    }
}
