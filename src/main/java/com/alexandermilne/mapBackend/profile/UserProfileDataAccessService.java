package com.alexandermilne.mapBackend.profile;

import com.alexandermilne.mapBackend.bucket.ImageBucketName;
import com.alexandermilne.mapBackend.bucket.VideoBucketName;
import com.alexandermilne.mapBackend.datastore.UserDao;
import com.alexandermilne.mapBackend.filestore.local.storage.StorageService;
import com.alexandermilne.mapBackend.filestore.s3.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserProfileDataAccessService {

    private final UserDao userDao;
    public FileStore awsFileStore;
    private final StorageService localStorageService;

    @Autowired
    public UserProfileDataAccessService(@Qualifier("fakeDao") UserDao userDao,
                                        FileStore awsFileStore, StorageService storageService) {
        this.userDao = userDao;
        this.awsFileStore = awsFileStore;
        this.localStorageService = storageService;
    }


    public int addUserProfile(UserProfile userProfile) {
        System.out.println(" personId: " + userProfile.getUserProfileId() + " person name: " + userProfile.getUsername());
        UUID id = userProfile.getUserProfileId();
        if (id == null){
            return addUserProfile(null, userProfile);
        } else {
            return addUserProfile(id, userProfile);
        }
    }

    public int addUserProfile(UUID userProfileId,  UserProfile userProfile) {
        userProfileId = Optional.ofNullable(userProfileId)
                .orElse(UUID.randomUUID());
        //if (person.getId().equals(null)){
        return userDao.insertUserProfile(userProfileId, userProfile);
    }


    public List<UserProfile> getUserProfiles() {
        return userDao.getUserProfiles();
    }

    private String getVideoPathFormat(UserProfile user) {
        return String.format("%s/%s", VideoBucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
    }

    public int uploadVideoToAws(UUID userProfileId, MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
        }

        if (file.getSize() > 536870912) {
            throw new IllegalStateException("file is greater than 500MB: " + file.getSize() );
        }
        //TODO file already uploaded
        if (false) { //file.getOriginalFilename()
            return 0;
        }

        //Check file is correct type
        //IMAGE_JPEG.getMineType()
        if (Arrays.asList("video/mp4").contains(file.getContentType())) {
            throw new IllegalStateException("File must be mp4 [" + file.getContentType() + "]");
        }

        //get the db user
        UserProfile user = getUserProfileOrThrow(userProfileId);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        //store image in AWS db
        String path = getPathFormat(user);
        String filename = String.format("%s", file.getOriginalFilename());
        try {
            awsFileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


        return userDao.uploadVideoToAws(userProfileId, file);
    }

    public byte[] downloadVideo(UUID userProfileId) {
        UserProfile user = getUserProfileOrThrow(userProfileId);
        String path = getPathFormat(user);

        return user.getUserProfileImageLink()
                .map(key -> awsFileStore.download(path, key))
                .orElse(new byte[0]);
    }

    public int uploadUserProfileImage(UUID userProfileId, MultipartFile file) {

        //Check file is not null
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
        }

        //Check file is correct type
        //IMAGE_JPEG.getMineType()
        if (Arrays.asList(IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF).contains(file.getContentType())) {
            throw new IllegalStateException("File must be jpg, Png, or Gif [" + file.getContentType() + "]");
        }

        //get the db user
        UserProfile user = getUserProfileOrThrow(userProfileId);


        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        //store image in AWS db
        String path = getPathFormat(user);
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            awsFileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


        return userDao.uploadUserProfileImage(userProfileId, file);
    }

    private String getPathFormat(UserProfile user) {
        return String.format("%s/%s", ImageBucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
    }

    private UserProfile getUserProfileOrThrow(UUID userProfileId) {
        return userDao
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst().orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
    }


    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile user = getUserProfileOrThrow(userProfileId);
        String path = getPathFormat(user);

        return user.getUserProfileImageLink()
                .map(key -> awsFileStore.download(path, key))
                .orElse(new byte[0]);
    }

    public int uploadUserVideo(UUID userId, MultipartFile file, int price, String regions) {
        String filename = StringUtils.stripFilenameExtension(StringUtils.cleanPath(file.getOriginalFilename()));
        if (!localStorageService.doesFileExist(userId, filename)) {
        System.out.println("1 ----- NOT Errored YET! in UserProfileDataAccessService.java -> uploadUserVideo");
        localStorageService.store(userId, file);

        System.out.println("2 ----- NOT Errored YET! in UserProfileDataAccessService.java -> uploadUserVideo");
        userDao.addVideoLink(userId, filename, price, regions);
        } else {
            System.out.println("File already uploaded in UserProfileDataAccessService.java -> uploadUserVideo");
        }
        return 0;
    }

    public UserVideo getSmartContract(UUID userId, String filename) {
        UserVideo videoInfo = userDao.getVideoInfo(userId, filename);
        return videoInfo;
    }

}
