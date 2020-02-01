package com.yunhongmin.kafka.models;

import com.yunhongmin.kafka.schemas.PullRequestSchema;
import org.json.JSONObject;

public class PullRequest {
    private String url;
    private String htmlUrl;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public static PullRequest fromJson(JSONObject json) {
        PullRequest pr = new PullRequest();
        pr.setHtmlUrl(json.getString(PullRequestSchema.PR_HTML_URL_FIELD));
        pr.setUrl(json.getString(PullRequestSchema.PR_URL_FIELD));
        return pr;
    }
}
