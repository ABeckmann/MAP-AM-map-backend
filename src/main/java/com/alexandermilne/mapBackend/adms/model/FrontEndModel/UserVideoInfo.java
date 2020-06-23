package com.alexandermilne.mapBackend.adms.model.FrontEndModel;

import com.alexandermilne.mapBackend.adms.model.AvailableLicense;
import com.alexandermilne.mapBackend.adms.model.UserLicense;

import java.util.List;
import java.util.UUID;

public class UserVideoInfo {
    public String title;
    public int price;
    public String regions;


    public UserVideoInfo(String title, int price, String regions) {
        this.title = title;
        this.price = price;
        this.regions = regions;
    }
}
