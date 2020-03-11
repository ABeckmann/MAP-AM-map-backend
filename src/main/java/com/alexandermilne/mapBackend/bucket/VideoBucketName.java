package com.alexandermilne.mapBackend.bucket;

public enum VideoBucketName {

    PROFILE_IMAGE("map-backend-video");

    private final String bucketName;

    VideoBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
