package com.alexandermilne.mapBackend.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.*;

public class UserProfile {

    private UUID userProfileId;
    @NotBlank
    private String username;
    private String userProfileImageLink; //s3 key
    private List<UserVideo> userVideoLinks;
    //private String userVideoLink;

    public UserProfile(@JsonProperty("userProfileId") UUID userProfileId, @JsonProperty("username") String username, @JsonProperty("userProfileImageLink") String userProfileImageLink) {
        this.userProfileId = userProfileId;
        this.username = username;
        this.userProfileImageLink = userProfileImageLink;
        this.userVideoLinks = new ArrayList<>();
    }

    public UserProfile(UUID userProfileId, String username) {
        this.userProfileId = userProfileId;
        this.username = username;
        this.userProfileImageLink = null;
        this.userVideoLinks = new ArrayList<>();
    }


    public UUID getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(UUID userProfileId) {
        this.userProfileId = userProfileId;
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

    public Optional<List<UserVideo>> getUserVideoLinks() {
        return Optional.ofNullable(userVideoLinks);
    }

    public void addUserVideoLink(UserVideo userVideoUpload) {
        this.userVideoLinks.add(userVideoUpload);
    }


    @Override
    public String toString() {
        return String.format("UserProfile ->  userProfileId: %s, username: %s, userProfileImageLink: %s, List<UserVideo>: %s", userProfileId, username, userProfileImageLink, userVideoLinks);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(userProfileId, that.userProfileId) &&
                Objects.equals(username,that.username) &&
                Objects.equals(userProfileImageLink,that.userProfileImageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileId, username, userProfileImageLink);
    }
}
