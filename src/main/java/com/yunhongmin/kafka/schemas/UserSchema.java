package com.yunhongmin.kafka.schemas;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

public class UserSchema {
    public static String SCHEMA_VALUE_USER = "user";

    public static String USER_URL_FIELD = "url";
    public static String USER_ID_FIELD = "id";
    public static String USER_LOGIN_FIELD = "login";

    public static Schema SCHEMA = SchemaBuilder.struct().name(SCHEMA_VALUE_USER)
            .version(1)
            .field(USER_URL_FIELD, Schema.STRING_SCHEMA)
            .field(USER_ID_FIELD, Schema.INT32_SCHEMA)
            .field(USER_LOGIN_FIELD, Schema.STRING_SCHEMA)
            .build();

}
