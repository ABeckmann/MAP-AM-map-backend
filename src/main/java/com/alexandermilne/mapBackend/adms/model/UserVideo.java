package com.alexandermilne.mapBackend.adms.model;

import java.util.List;
import java.util.UUID;

public class UserVideo {
    public UUID Id;
    public UUID videoOwnerId;
    public String title;
    public String localStorageLocation;

    public List<UserLicense> userLicences;
    public List<UserVideoLicence> availableLicenses;

    public UserVideo(UUID id, UUID videoOwnerId, String title, String localStorageLocation) {
        Id = id;
        this.videoOwnerId = videoOwnerId;
        this.title = title;
        this.localStorageLocation = localStorageLocation;
//        this.userLicences = userLicences;
//        this.availableLicenses = availableLicenses;
    }

    public void setUserLicences(List<UserLicense> userLicences){
        this.userLicences=userLicences;
    }
    public void setAvailableLicense(List<UserVideoLicence> availableLicenses){
        this.availableLicenses=availableLicenses;
    }
}
