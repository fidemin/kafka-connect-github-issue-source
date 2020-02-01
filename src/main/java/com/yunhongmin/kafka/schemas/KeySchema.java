package com.yunhongmin.kafka.schemas;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

import static com.yunhongmin.kafka.schemas.IssueSchema.NUMBER_FIELD;

public class KeySchema {
    public static String SCHEMA_KEY = "issue_key";

    public static String OWNER_FIELD = "owner";
    public static String REPOSITORY_FIELD = "repository";

    public static Schema SCHEMA = SchemaBuilder.struct().name(SCHEMA_KEY)
            .version(1)
            .field(OWNER_FIELD, Schema.STRING_SCHEMA)
            .field(REPOSITORY_FIELD, Schema.STRING_SCHEMA)
            .field(NUMBER_FIELD, Schema.INT32_SCHEMA)
            .build();

}
