package com.alexandermilne.mapBackend.adms.model.FrontEndModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MyLicencesByVideoVM {
    public UUID videoId;
    public String videoTitle;
    public List<AvailableLicensesVM> licences;

    public MyLicencesByVideoVM(UUID videoId, String videoTitle) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.licences = new ArrayList<>();
    }

    public MyLicencesByVideoVM(UUID videoId, List<AvailableLicensesVM> licences) {
        this.videoId = videoId;
        this.licences = licences;
    }

    public void addLicence(AvailableLicensesVM alVM){
        this.licences.add(alVM);
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
