package com.alexandermilne.mapBackend.adms.service;

import com.alexandermilne.mapBackend.adms.dao.Dao;
import com.alexandermilne.mapBackend.adms.model.FrontEndModel.AvailableLicensesVM;
import com.alexandermilne.mapBackend.adms.model.FrontEndModel.MyLicencesByVideoVM;
import com.alexandermilne.mapBackend.adms.model.FrontEndModel.UserVideoInfo;
import com.alexandermilne.mapBackend.adms.service.filestore.local.storage.StorageService;
import com.alexandermilne.mapBackend.adms.service.filestore.s3.FileStore;
import com.alexandermilne.mapBackend.adms.model.User;
import com.alexandermilne.mapBackend.adms.model.UserVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.List;

@Service
public class UserProfileDataAccessService {

    private final Dao dao;
    //public FileStore awsFileStore;
    private final StorageService localStorageService;

    @Autowired
    public UserProfileDataAccessService(@Qualifier("PostgresDao") Dao dao,
                                        FileStore awsFileStore, StorageService storageService) {
        this.dao = dao;
        //this.awsFileStore = awsFileStore;
        this.localStorageService = storageService;
    }


//    public int addUser(User user) {
//        System.out.println(" personId: " + user.getUserId() + " person name: " + user.getUsername());
//        UUID id = user.getUserId();
//        if (id == null){
//            return addUser(null, user);
//        } else {
//            return addUser(id, user);
//        }
//    }

//    public int addUser(UUID userId, User user) {
//        userId = Optional.ofNullable(userId)
//                .orElse(UUID.randomUUID());
//        //if (person.getId().equals(null)){
//        return dao.insertUser(userId, user);
//    }


    public List<User> getUsers() {
        return dao.getAllUsers();
    }

    public int getUserAccountBalance(UUID userId) {
        return dao.getUserAccountBalance(userId);

    }

    public List<UserVideo> getAllVideos() {
        return dao.getAllVideos();
    }

//    private String getVideoPathFormat(User user) {
//        return String.format("%s/%s", VideoBucketName.PROFILE_IMAGE.getBucketName(), user.getUserId());
//    }

//    public int uploadVideoToAws(UUID userProfileId, MultipartFile file) {
//
//        if (file.isEmpty()) {
//            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
//        }
//
//        if (file.getSize() > 536870912) {
//            throw new IllegalStateException("file is greater than 500MB: " + file.getSize() );
//        }
//        //TODO file already uploaded
//        if (false) { //file.getOriginalFilename()
//            return 0;
//        }
//
//        //Check file is correct type
//        //IMAGE_JPEG.getMineType()
//        if (Arrays.asList("video/mp4").contains(file.getContentType())) {
//            throw new IllegalStateException("File must be mp4 [" + file.getContentType() + "]");
//        }
//
//        //get the db user
//        User user = getUserProfileOrThrow(userProfileId);
//
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("Content-Type", file.getContentType());
//        metadata.put("Content-Length", String.valueOf(file.getSize()));
//
//        //store image in AWS db
//        String path = getPathFormat(user);
//        String filename = String.format("%s", file.getOriginalFilename());
//        try {
//            awsFileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
//            user.setUserProfileImageLink(filename);
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
//
//
//        return dao.uploadVideoToAws(userProfileId, file);
//    }

//    public byte[] downloadVideo(UUID userProfileId) {
//        User user = getUserProfileOrThrow(userProfileId);
//        String path = getPathFormat(user);
//
//        return user.getUserProfileImageLink()
//                .map(key -> awsFileStore.download(path, key))
//                .orElse(new byte[0]);
//    }

//    public int uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
//
//        //Check file is not null
//        if (file.isEmpty()) {
//            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
//        }
//
//        //Check file is correct type
//        //IMAGE_JPEG.getMineType()
//        if (Arrays.asList(IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF).contains(file.getContentType())) {
//            throw new IllegalStateException("File must be jpg, Png, or Gif [" + file.getContentType() + "]");
//        }
//
//        //get the db user
//        User user = getUserProfileOrThrow(userProfileId);
//
//
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("Content-Type", file.getContentType());
//        metadata.put("Content-Length", String.valueOf(file.getSize()));
//
//        //store image in AWS db
//        String path = getPathFormat(user);
//        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
//        try {
//            awsFileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
//            user.setUserProfileImageLink(filename);
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
//
//
//        return dao.uploadUserImage(userProfileId, file);
//    }

//    public byte[] downloadUserProfileImage(UUID userProfileId) {
//        User user = getUserProfileOrThrow(userProfileId);
//        String path = getPathFormat(user);
//
//        return user.getUserProfileImageLink()
//                .map(key -> awsFileStore.download(path, key))
//                .orElse(new byte[0]);
//    }


//    private String getPathFormat(User user) {
//        return String.format("%s/%s", ImageBucketName.PROFILE_IMAGE.getBucketName(), user.getUserId());
//    }
//
//    private User getUserProfileOrThrow(UUID userProfileId) {
//        return dao
//                .getAllUsers()
//                .stream()
//                .filter(userProfile -> userProfile.getUserId().equals(userProfileId))
//                .findFirst().orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
//    }

    public int uploadUserVideo(UUID userId, MultipartFile file, int price, String regions) throws Exception {
        String filename = StringUtils.stripFilenameExtension(StringUtils.cleanPath(file.getOriginalFilename()));
//        if (!localStorageService.doesFileExist(userId, filename)) {
//            System.out.println("1 ----- NOT Errored YET! in UserProfileDataAccessService.java -> uploadUserVideo");
//            localStorageService.store(userId, file);
//
//            System.out.println("2 ----- NOT Errored YET! in UserProfileDataAccessService.java -> uploadUserVideo");
//            dao.addVideoLink(userId, filename, price, regions);
//        } else {
//            System.out.println("File already uploaded in UserProfileDataAccessService.java -> uploadUserVideo");
//        }
        Optional<User> maybeU = dao.getUserById(userId);
        if (maybeU.isEmpty()) {
            throw new Exception(String.format("there is no user %s", userId));
        }
        //User u = maybeU.get();

        List<UserVideo> userVideos = dao.getAllUserVideos(userId);

        String strStorageLocation = localStorageService.getStoragePath(userId, filename).toString();
        //        if (u.getUserVideos().get().stream().map(x->x.localStorageLocation).collect(Collectors.toList()).contains(strStorageLocation)) {
        if (userVideos.stream().anyMatch(x->x.localStorageLocation.equals(strStorageLocation))) {
            System.out.println("File already uploaded in UserProfileDataAccessService.java -> uploadUserVideo");
            throw new Exception(String.format("there is already a video %s, stored at %s", filename, strStorageLocation));
        }
        System.out.println("1 ----- NOT Errored YET! in UserProfileDataAccessService.java -> uploadUserVideo");
        localStorageService.store(userId, file);

        System.out.println("2 ----- NOT Errored YET! in UserProfileDataAccessService.java -> uploadUserVideo");
        UUID videoId = dao.addVideo(userId, filename, strStorageLocation);

        String[] regionsArray = regions.split(",");

        for (String r : regionsArray
                ) {
            dao.addAvailableLicence(videoId, price, r);
        }
        System.out.println("3 ----- Done");
        return 0;
    }

    public UserVideoInfo getSmartContract(UUID userId, String filename) {
        UserVideoInfo videoInfo = dao.getVideoInfo(userId, filename);
        return videoInfo;
    }

    public List<AvailableLicensesVM> getAvailableLicence(UUID videoId) {
        return dao.getAvailableLicences(videoId);
    }

    public List<MyLicencesByVideoVM> getMyLicences(UUID videoId) {
        return dao.getMyLicences(videoId);
    }


    public String getVideoStorageLocation(UUID videoId) throws Exception {
        return dao.getVideoStorageLocation(videoId).orElseThrow(() -> new Exception("video does not have a local storage location"));
    }

    public void purchaseLicence(UUID purchasingUser, UUID licenceId) {
        dao.purchaseLicence(purchasingUser, licenceId);
    }
}
