package com.alexandermilne.mapBackend.bucket;

public enum ImageBucketName {

    PROFILE_IMAGE("testing-image-upload");

    private final String bucketName;

    ImageBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
