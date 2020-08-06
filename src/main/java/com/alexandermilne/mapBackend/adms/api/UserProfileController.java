package com.alexandermilne.mapBackend.adms.api;

import com.alexandermilne.mapBackend.adms.model.User;
import com.alexandermilne.mapBackend.adms.service.UserProfileDataAccessService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000", "http://localhost"})
@RequestMapping("api/v1/user-profile")
public class UserProfileController {

    private final UserProfileDataAccessService dataAccessService;

    public UserProfileController(UserProfileDataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

    @PostMapping(
            path = "accountBalance")
    public int getAccountBalance(@RequestParam("userId") UUID userId) {
        return dataAccessService.getUserAccountBalance(userId);
        //return dataAccessService.downloadUserProfileImage(userProfileId);
    }

    @PostMapping
    public void addUserProfile(@RequestBody @Valid @NotNull User user) {
        //dataAccessService.addUser(user);
    }

    @GetMapping
    public List<User> getUserProfiles() {
        return dataAccessService.getUsers();
    }

    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public int uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
                                      @RequestParam("file") MultipartFile file) {

        //return dataAccessService.uploadUserProfileImage(userProfileId, file);
        return 0;
    }

    @GetMapping(
            path = "{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId) {
        return null;
        //return dataAccessService.downloadUserProfileImage(userProfileId);
    }




}
