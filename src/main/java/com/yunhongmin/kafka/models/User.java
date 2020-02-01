package com.yunhongmin.kafka.models;

import com.yunhongmin.kafka.schemas.UserSchema;
import org.json.JSONObject;

public class User {
    private String url;
    private Integer id;
    private String login;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    static User fromJson(JSONObject json) {
        User user = new User();
        user.setId(json.getInt(UserSchema.USER_ID_FIELD));
        user.setUrl(json.getString(UserSchema.USER_URL_FIELD));
        user.setLogin(json.getString(UserSchema.USER_LOGIN_FIELD));
        return user;
    }
}
