package com.alexandermilne.mapBackend.adms.dao;


import com.alexandermilne.mapBackend.adms.model.FrontEndModel.AvailableLicensesVM;
import com.alexandermilne.mapBackend.adms.model.FrontEndModel.UserVideoInfo;
import com.alexandermilne.mapBackend.adms.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Repository("fakeDao")
public class FakeDataStore implements Dao {

    private static List<User> USER_PROFILES = new ArrayList<>();

    public FakeDataStore(){
        USER_PROFILES.add(new User(UUID.fromString("93bf9ae1-2a1f-47b1-83f6-e9fe4ba1710a"),"jamesBond",null, 100));
        USER_PROFILES.add(new User(UUID.fromString("6203c0f4-63d1-4d51-91f5-64de0ee6073d"),"bobNameless",null, 200));
    }


    @Override
    public int insertUser(UUID id, User user) {
        USER_PROFILES.add(new User(id, user.getUsername()));
        return 1;
    }


    @Override
    public List<User> getAllUsers(){
        return USER_PROFILES;
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return Optional.empty();
    }

    @Override
    public int uploadUserImage(UUID userProfileId, MultipartFile file) {

        return 0;
    }
    @Override
    public int uploadVideoToAws(UUID userProfileId, MultipartFile file) {

        return 0;
    }


    @Override
    public int addAvailableLicence(UUID videoId, int price, String regions) {
        return 0;
    }

    @Override
    public UserVideoInfo getVideoInfo(UUID userId, String filename) {
        return null;
    }

    @Override
    public Optional<String> getVideoStorageLocation(UUID videoId) {
        return Optional.empty();
    }

    @Override
    public List<AvailableLicensesVM> getAvailableLicences(UUID videoId) {
        return null;
    }

    @Override
    public UUID addVideo(UUID userProfileId, String filename, String strStorageLocation) {//, int price, String regions
        System.out.println(String.format("3 ---- No Error yet!!! FakeUserProfileDataStore -> addVideoLink || userProfileId: %s, filename: %s", userProfileId, filename));
        System.out.println("USER_PROFILES.toString(): ");
        for(User model : USER_PROFILES) {
            System.out.println(model.toString());
        }
        System.out.println(String.format("4 ---- No Error yet!!!USER_PROFILES matching: %s", USER_PROFILES.stream().filter(x -> x.getUserId() == userProfileId)));

        User u = null;
        for (User tempU: USER_PROFILES
             ) {
            if (tempU.getUserId().equals(userProfileId)) {
                u= tempU;
            }

        }
        //UserProfile u = USER_PROFILES.stream().filter(x -> x.getUserProfileId() == userProfileId).findFirst().get();
        System.out.println(String.format("5 ---- No Error yet!!!USER_PROFILES matching: %s", u));

        //u.addUserVideoLink(new UserVideo(filename, price, regions));
        return null;
    }

//    @Override
//    public UserVideo getVideoInfo(UUID userProfileId, String filename) {
//        User u = null;
//        for (User tempU: USER_PROFILES)
//        {
//            if (tempU.getUserId().equals(userProfileId)) {
//                u= tempU;
//            }
//
//        }
//        if (u==null) {
//            return null;
//        }
//        Optional<List<UserVideo>> UserVideos = u.getUserVideos();
//        if (UserVideos.isEmpty()){
//            return null;
//        }
//        UserVideo Video = null;
//        for (UserVideo tempV: UserVideos.get())
//        {
//            if (tempV.filename.equals(filename)) {
//                Video= tempV;
//            }
//
//        }
//        if (Video==null) {
//            return null;
//        }
//        return Video;
//
//    }
}
