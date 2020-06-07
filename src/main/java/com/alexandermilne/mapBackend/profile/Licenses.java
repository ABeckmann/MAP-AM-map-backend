package com.alexandermilne.mapBackend.profile;

public class Licenses {
    //public UUID userProfileId;
    public String filename;
    public int price;
    public String regions;
    public UserVideo userVideo;
    //public String userVideoLink;

    public Licenses(String name, int price, String regions) { //UUID userProfileId, , String userVideoLink
        //this.userProfileId = userProfileId;
        this.filename = name;
        this.price = price;
        this.regions = regions;

       // this.userVideoLink = userVideoLink;
    }
}
