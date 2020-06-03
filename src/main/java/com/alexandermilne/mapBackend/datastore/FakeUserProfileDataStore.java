package com.alexandermilne.mapBackend.datastore;


import com.alexandermilne.mapBackend.profile.UserProfile;
import com.alexandermilne.mapBackend.profile.UserVideo;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Repository("fakeDao")
public class FakeUserProfileDataStore implements UserDao {

    private static List<UserProfile> USER_PROFILES = new ArrayList<>();

    public FakeUserProfileDataStore(){
        USER_PROFILES.add(new UserProfile(UUID.fromString("93bf9ae1-2a1f-47b1-83f6-e9fe4ba1710a"),"jamesBond",null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("6203c0f4-63d1-4d51-91f5-64de0ee6073d"),"bobNameless",null));
    }


    @Override
    public int insertUserProfile(UUID id, UserProfile user) {
        USER_PROFILES.add(new UserProfile(id, user.getUsername()));
        return 1;
    }


    @Override
    public List<UserProfile> getUserProfiles(){
        return USER_PROFILES;
    }

    @Override
    public int uploadUserProfileImage(UUID userProfileId, MultipartFile file) {

        return 0;
    }
    @Override
    public int uploadVideoToAws(UUID userProfileId, MultipartFile file) {

        return 0;
    }

    @Override
    public int addVideoLink(UUID userProfileId, String filename, int price, String regions) {
        System.out.println(String.format("3 ---- No Error yet!!! FakeUserProfileDataStore -> addVideoLink || userProfileId: %s, filename: %s", userProfileId, filename));
        System.out.println("USER_PROFILES.toString(): ");
        for(UserProfile model : USER_PROFILES) {
            System.out.println(model.toString());
        }
        System.out.println(String.format("4 ---- No Error yet!!!USER_PROFILES matching: %s", USER_PROFILES.stream().filter(x -> x.getUserProfileId() == userProfileId)));

        UserProfile u = null;
        for (UserProfile tempU: USER_PROFILES
             ) {
            if (tempU.getUserProfileId().equals(userProfileId)) {
                u= tempU;
            }

        }
        //UserProfile u = USER_PROFILES.stream().filter(x -> x.getUserProfileId() == userProfileId).findFirst().get();
        System.out.println(String.format("5 ---- No Error yet!!!USER_PROFILES matching: %s", u));

        u.addUserVideoLink(new UserVideo(filename, price, regions));
        return 0;
    }

    @Override
    public UserVideo getVideoInfo(UUID userProfileId, String filename) {
        UserProfile u = null;
        for (UserProfile tempU: USER_PROFILES)
        {
            if (tempU.getUserProfileId().equals(userProfileId)) {
                u= tempU;
            }

        }
        if (u==null) {
            return null;
        }
        Optional<List<UserVideo>> UserVideos = u.getUserVideoLinks();
        if (UserVideos.isEmpty()){
            return null;
        }
        UserVideo Video = null;
        for (UserVideo tempV: UserVideos.get())
        {
            if (tempV.filename.equals(filename)) {
                Video= tempV;
            }

        }
        if (Video==null) {
            return null;
        }
        return Video;

    }
}
