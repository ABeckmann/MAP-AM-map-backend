package com.alexandermilne.mapBackend.adms.model.FrontEndModel;

import com.alexandermilne.mapBackend.adms.model.AvailableLicense;

import java.util.List;
import java.util.UUID;

public class AvailableLicensesVM {
    public UUID licenceId;
    public UUID ownerId;
    public int price;
    public String region;

    public AvailableLicensesVM(UUID licenceId, int price, String region, UUID ownerId) {
        this.licenceId = licenceId;
        this.price = price;
        this.region = region;
        this.ownerId = ownerId;
    }
    //public UUID videoId;



//    public AvailableLicenses(UUID id, int price, String regions, UUID videoId) {//, UserVideo userVideo
//        Id = id;
//        this.price = price;
//        this.regions = regions;
//        this.videoId = videoId;
////        this.userVideo = userVideo;
//    }
}
