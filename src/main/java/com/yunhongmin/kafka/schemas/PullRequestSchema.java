package com.yunhongmin.kafka.schemas;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

public class PullRequestSchema {
    public static String SCHEMA_VALUE_PR = "pr";


    public static String PR_URL_FIELD = "url";
    public static String PR_HTML_URL_FIELD = "html_url";

    public static Schema SCHEMA = SchemaBuilder.struct().name(SCHEMA_VALUE_PR)
            .version(1)
            .field(PR_URL_FIELD, Schema.STRING_SCHEMA)
            .field(PR_HTML_URL_FIELD, Schema.STRING_SCHEMA)
            .optional()  // for optional schema
            .build();


}
