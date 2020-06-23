package com.alexandermilne.mapBackend.adms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.*;

public class User {

    private UUID userId;
    @NotBlank
    private String username;
    private String userProfileImageLink; //s3 key
    private int userMoney;
    private List<UserVideo> userVideos;
    //private List<UserLicense> userLicenses;
    //private String userVideoLink;

    public User(@JsonProperty("userProfileId") UUID userId, @JsonProperty("username") String username, @JsonProperty("userProfileImageLink") String userProfileImageLink, @JsonProperty("userMoney") int userMoney) {
        this.userId = userId;
        this.username = username;
        this.userProfileImageLink = userProfileImageLink;
        this.userMoney = userMoney;
        this.userVideos = new ArrayList<>();
//        this.userLicenses = new ArrayList<>();
    }

    public User(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
        this.userProfileImageLink = null;
        this.userVideos = new ArrayList<>();
//        this.userLicenses = new ArrayList<>();
    }

//    public User(UUID userId, String username, String userProfileImageLink, List<UserVideo> userVideos, List<UserLicense> userLicenses) {
//        this.userId = userId;
//        this.username = username;
//        this.userProfileImageLink = userProfileImageLink;
//        this.userVideos = userVideos;
//        this.userLicenses = userLicenses;
//    }

//    public void setUserVideos(List<UserVideo> uv){
//        this.userVideos = uv;
//    }

//    public void setUserLicenses(List<UserLicense> ul){
//        this.userLicenses = ul;
//    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Optional<String> getUserProfileImageLink() {
        return Optional.ofNullable(userProfileImageLink);
    }

    public void setUserProfileImageLink(String userProfileImageLink) {
        this.userProfileImageLink = userProfileImageLink;
    }

    public Optional<List<UserVideo>> getUserVideos() {
        return Optional.ofNullable(userVideos);
    }

    public void setUserVideos(List<UserVideo> uv){
       this.userVideos = uv;
    }

    public void addUserVideo(UserVideo userVideoUpload) {
        this.userVideos.add(userVideoUpload);
    }


    @Override
    public String toString() {
        return String.format("UserProfile ->  userProfileId: %s, username: %s, userProfileImageLink: %s, List<UserVideo>: %s", userId, username, userProfileImageLink, userVideos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(username,that.username) &&
                Objects.equals(userProfileImageLink,that.userProfileImageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, userProfileImageLink);
    }
}
