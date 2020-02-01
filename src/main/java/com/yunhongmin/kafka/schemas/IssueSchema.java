package com.yunhongmin.kafka.schemas;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

public class IssueSchema {
    public static String SCHEMA_VALUE_ISSUE = "issue";


    public static String CREATED_AT_FIELD = "created_at";
    public static String UPDATED_AT_FIELD = "updated_at";
    public static String NUMBER_FIELD = "number";
    public static String URL_FIELD = "url";
    public static String TITLE_FIELD = "title";
    public static String STATE_FIELD = "state";
    public static String USER_FIELD = "user";
    public static String PR_FIELD = "pull_request";

    public static Schema SCHEMA = SchemaBuilder.struct().name(SCHEMA_VALUE_ISSUE)
            .version(1)
            .field(URL_FIELD, Schema.STRING_SCHEMA)
            .field(TITLE_FIELD, Schema.STRING_SCHEMA)
            .field(CREATED_AT_FIELD, Schema.INT64_SCHEMA)
            .field(UPDATED_AT_FIELD, Schema.INT64_SCHEMA)
            .field(NUMBER_FIELD, Schema.INT32_SCHEMA)
            .field(STATE_FIELD, Schema.STRING_SCHEMA)
            .field(USER_FIELD, UserSchema.SCHEMA) // mandatory
            .field(PR_FIELD, PullRequestSchema.SCHEMA)     // optional
            .build();
}
