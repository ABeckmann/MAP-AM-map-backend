package com.alexandermilne.awsimageupload.datastore;

import com.alexandermilne.awsimageupload.profile.UserProfile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

    int insertUserProfile(UUID id, UserProfile person);
     /**
    default int insertPerson(Person person) {
        UUID id = UUID.randomUUID();
        return  insertPerson(id, person);
   }
     **/
    public List<UserProfile> getUserProfiles();

    int uploadUserProfileImage(UUID userProfileId, MultipartFile file);
    //List<Person> selectAllPeople();
/**
    Optional<Person> selectPersonById(UUID id);

    int deletePersonById(UUID id);

    int updatePersonById(UUID id, Person person);
 **/
}
