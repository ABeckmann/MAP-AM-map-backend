package com.alexandermilne.mapBackend.adms.api;

import com.alexandermilne.mapBackend.adms.model.FrontEndModel.AvailableLicensesVM;
import com.alexandermilne.mapBackend.adms.model.FrontEndModel.MyLicencesByVideoVM;
import com.alexandermilne.mapBackend.adms.model.FrontEndModel.UserVideoInfo;
import com.alexandermilne.mapBackend.adms.service.filestore.local.VideosIconsDisplaySrc;
import com.alexandermilne.mapBackend.adms.service.filestore.local.storage.StorageFileNotFoundException;
import com.alexandermilne.mapBackend.adms.service.filestore.local.storage.StorageService;
import com.alexandermilne.mapBackend.adms.model.User;
import com.alexandermilne.mapBackend.adms.service.UserProfileDataAccessService;
import com.alexandermilne.mapBackend.adms.model.UserVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000", "http://localhost"})
@RequestMapping("api/v1/user-videos")
public class FileUploadController {
    private final StorageService storageService;
    private final UserProfileDataAccessService dataAccessService;

    @Autowired
    public FileUploadController(StorageService storageService, UserProfileDataAccessService dataAccessService) {
        this.storageService = storageService;
        this.dataAccessService = dataAccessService;
    }

//    @GetMapping("/")
//    public List<UserVideos> listUploadedFilesById() throws IOException {
//        List<UserProfile> users = dataAccessService.getUserProfiles();
//        List<UserVideos> userToVideoMapping = new ArrayList<>();
//
//        for (UserProfile u: users
//             ) {
//            if (u.getUserVideoLinks().isPresent()) {
//                for (String vid: u.getUserVideoLinks().get()
//                ) {
//                    userToVideoMapping.add(new UserVideos(u.getUserProfileId(), vid));
//                }
//
//            }
//
//        }
//        return userToVideoMapping;
//    }

    @GetMapping("/icons")
    public List<VideosIconsDisplaySrc> vidIcons() {
        //List<User> users = dataAccessService.getUsers();
        List<UserVideo> videos = dataAccessService.getAllVideos();
        List<VideosIconsDisplaySrc> videoIcons = new ArrayList<>();

//        for (User u: users
//        ) {
//            if (u.getUserVideos().isPresent()) {
//                for (UserVideo vid: u.getUserVideos().get()
//                ) {
//                    videoIcons.add(new VideosIconsDisplaySrc(vid.Id.toString(), u.getUserId().toString(), vid.title)); //, storageService.getIconLink(u.getUserProfileId(), vid.filename).toString())
//                }
//            }
//
//        }
        for (UserVideo vid: videos
        ) {
            videoIcons.add(new VideosIconsDisplaySrc(vid.Id.toString(), vid.videoOwnerId.toString(), vid.title)); //, storageService.getIconLink(u.getUserProfileId(), vid.filename).toString())
        }
        return videoIcons;
    }
    @GetMapping("/icons/{userId}")
    public List<VideosIconsDisplaySrc> vidIcons(@PathVariable UUID userId) {
        List<User> users = dataAccessService.getUsers();
        List<VideosIconsDisplaySrc> videoIcons = new ArrayList<>();

        for (User u: users
        ) {
            if (u.getUserId().equals(userId)) {
                if (u.getUserVideos().isPresent()) {
                    for (UserVideo vid: u.getUserVideos().get()
                    ) {
                        videoIcons.add(new VideosIconsDisplaySrc(vid.Id.toString(), u.getUserId().toString(), vid.title)); //, storageService.getIconLink(u.getUserProfileId(), vid.filename).toString())
                    }
                }
            }
        }
        return videoIcons;
    }


    @GetMapping("/{userId}/{filename:.+}/icon/download")
    public byte[] downloadVideoIcon( //@PathVariable("userProfileId") UUID userProfileId
            @PathVariable String filename,
            @PathVariable UUID userId) throws IOException {

        return storageService.loadIconAsResource(userId, filename).getInputStream().readAllBytes();
               // dataAccessService.downloadUserProfileImage(userProfileId);
    }

    @GetMapping("/{userId}/{filename:.+}/icon")
    @ResponseBody public ResponseEntity<Resource> serveIcon(
            @PathVariable String filename,
            @PathVariable UUID userId) {
        System.out.println(String.format("serveIcon ->>>>  userId: %s, fileName: %s", userId, filename));
        Resource file = storageService.loadIconAsResource(userId, filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
               "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/{filename:.+}/video")
    @ResponseBody public ResponseEntity<Resource> serveVideo(
            @PathVariable String filename) {
        System.out.println(String.format("serveVideo ->>>>  fileName: %s", filename));
        Resource file = storageService.loadVideoAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @GetMapping("/{userId}/{filename:.+}/video")
    @ResponseBody public ResponseEntity<Resource> serveVideo(
            @PathVariable String filename,
            @PathVariable UUID userId) {
        System.out.println(String.format("serveVideo ->>>>  fileName: %s", filename));
        Resource file = storageService.loadVideoAsResource(userId,filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    @GetMapping("/video/{videoId}")
    @ResponseBody public ResponseEntity<Resource> serveVideo(
            @PathVariable UUID videoId) {
        System.out.println(String.format("serveVideo ->>>>  videoId: %s", videoId));
        try {
            String videoStorageLocation = dataAccessService.getVideoStorageLocation(videoId);
            Resource file = storageService.loadVideoAsResource(Paths.get(videoStorageLocation,"video.mp4"));
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/{userId}/{filename:.+}/smartcontract")
    @ResponseBody public UserVideoInfo giveSmartContract(
            @PathVariable String filename,
            @PathVariable UUID userId) {
        return dataAccessService.getSmartContract(userId,filename);

    }

    @GetMapping("/smartcontract/{videoId}")
    @ResponseBody public List<AvailableLicensesVM> giveSmartContract(
            @PathVariable UUID videoId) {
        return dataAccessService.getAvailableLicence(videoId);
        //return dataAccessService.getSmartContract(userId,filename);

    }

    @GetMapping("/myLicences/{userId}")
    @ResponseBody public List<MyLicencesByVideoVM> getMyLicences(
            @PathVariable UUID userId) {
        return dataAccessService.getMyLicences(userId);
        //return dataAccessService.getSmartContract(userId,filename);

    }




    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public void handleFileUpload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("userId") UUID userId,
                                 @RequestParam("price") int price,
                                 @RequestParam("regions") String regions,
                                   RedirectAttributes redirectAttributes) {

        try {
            dataAccessService.uploadUserVideo(userId, file, price, regions);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/purchaseLicence")
    public void handleFileUpload(@RequestParam("purchasingUser") UUID purchasingUser,
                                 @RequestParam("licenceId") UUID licenceId
    ) {

        dataAccessService.purchaseLicence(purchasingUser, licenceId);


    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}




















