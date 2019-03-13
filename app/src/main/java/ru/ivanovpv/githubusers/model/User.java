package ru.ivanovpv.githubusers.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private Integer id;
    @SerializedName("avatar_url")
    private String avatarUrl;
    private String login;
    @SerializedName("html_url")
    private String pageUrl;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getLogin() {
        return login;
    }

    public Integer getId() {
        return id;
    }

}
