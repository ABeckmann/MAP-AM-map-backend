package com.alexandermilne.mapBackend.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class UserVideo {
    //public UUID userProfileId;
    public String filename;
    public int price;
    public String regions;
    //public String userVideoLink;

    public UserVideo(String name, int price, String regions) { //UUID userProfileId, , String userVideoLink
        //this.userProfileId = userProfileId;
        this.filename = name;
        this.price = price;
        this.regions = regions;

       // this.userVideoLink = userVideoLink;
    }
}
