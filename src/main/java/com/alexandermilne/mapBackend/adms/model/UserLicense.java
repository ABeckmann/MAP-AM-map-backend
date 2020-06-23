package com.alexandermilne.mapBackend.adms.model;

import java.util.UUID;

public class UserLicense {
    public UUID Id;
    public int price;
    public String region;

    public UUID licenceOwnerId;
    public UUID videoId;

    public UserLicense(UUID id, int price, String region, UUID licenceOwnerId, UUID videoId) {
        Id = id;
        this.price = price;
        this.region = region;

        this.licenceOwnerId = licenceOwnerId;
        this.videoId = videoId;

    }


}
