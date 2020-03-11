package com.alexandermilne.awsimageupload.datastore;


import com.alexandermilne.awsimageupload.profile.UserProfile;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.apache.http.entity.ContentType.*;

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

}
