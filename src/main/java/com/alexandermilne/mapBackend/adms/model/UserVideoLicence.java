package com.alexandermilne.mapBackend.adms.model;

import java.util.UUID;

public class UserVideoLicence {
    public UUID Id;

    public int price;
    public String region;


    public UUID videoId;
//    public UserVideo userVideo;
    //public String userVideoLink;


    public UserVideoLicence(UUID id, int price, String region, UUID videoId) {//, UserVideo userVideo
        Id = id;
        this.price = price;
        this.region = region;
        this.videoId = videoId;

//        this.userVideo = userVideo;
    }
}
