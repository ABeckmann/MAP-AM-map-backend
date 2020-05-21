package com.alexandermilne.mapBackend.profile;

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

    @PostMapping
    public void addUserProfile(@RequestBody @Valid @NotNull UserProfile userProfile) {
        dataAccessService.addUserProfile(userProfile);
    }

    @GetMapping
    public List<UserProfile> getUserProfiles() {
        return dataAccessService.getUserProfiles();
    }

    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public int uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
                                      @RequestParam("file") MultipartFile file) {

        return dataAccessService.uploadUserProfileImage(userProfileId, file);
    }

    @GetMapping(
            path = "{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId) {

        return dataAccessService.downloadUserProfileImage(userProfileId);
    }

}
