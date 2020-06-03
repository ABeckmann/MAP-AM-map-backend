package com.alexandermilne.mapBackend.filestore.local;

import com.alexandermilne.mapBackend.filestore.local.storage.StorageFileNotFoundException;
import com.alexandermilne.mapBackend.filestore.local.storage.StorageService;
import com.alexandermilne.mapBackend.profile.UserProfile;
import com.alexandermilne.mapBackend.profile.UserProfileDataAccessService;
import com.alexandermilne.mapBackend.profile.UserVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
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
        List<UserProfile> users = dataAccessService.getUserProfiles();
        List<VideosIconsDisplaySrc> videoIcons = new ArrayList<>();

        for (UserProfile u: users
        ) {
            if (u.getUserVideoLinks().isPresent()) {
                for (UserVideo vid: u.getUserVideoLinks().get()
                ) {
                    videoIcons.add(new VideosIconsDisplaySrc(u.getUserProfileId().toString(), vid.filename)); //, storageService.getIconLink(u.getUserProfileId(), vid.filename).toString())
                }
            }

        }
        return videoIcons;
    }
    @GetMapping("/icons/{userId}")
    public List<VideosIconsDisplaySrc> vidIcons(@PathVariable UUID userId) {
        List<UserProfile> users = dataAccessService.getUserProfiles();
        List<VideosIconsDisplaySrc> videoIcons = new ArrayList<>();

        for (UserProfile u: users
        ) {
            if (u.getUserProfileId()==userId) {
                if (u.getUserVideoLinks().isPresent()) {
                    for (UserVideo vid: u.getUserVideoLinks().get()
                    ) {
                        videoIcons.add(new VideosIconsDisplaySrc(u.getUserProfileId().toString(), vid.filename)); //, storageService.getIconLink(u.getUserProfileId(), vid.filename).toString())
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

    @GetMapping("/{userId}/{filename:.+}/smartcontract")
    @ResponseBody public UserVideo giveSmartContract(
            @PathVariable String filename,
            @PathVariable UUID userId) {
        return dataAccessService.getSmartContract(userId,filename);

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

        dataAccessService.uploadUserVideo(userId, file, price, regions);

    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}




















