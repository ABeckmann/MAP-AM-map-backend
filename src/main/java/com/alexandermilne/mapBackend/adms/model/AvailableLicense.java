package com.alexandermilne.mapBackend.adms.model;

import java.util.UUID;

public class AvailableLicense {
    public UUID Id;
    public int price;
    public String regions;

    public UUID videoId;
//    public UserVideo userVideo;
    //public String userVideoLink;


    public AvailableLicense(UUID id, int price, String regions, UUID videoId) {//, UserVideo userVideo
        Id = id;
        this.price = price;
        this.regions = regions;
        this.videoId = videoId;
//        this.userVideo = userVideo;
    }
}
