package com.alexandermilne.mapBackend.datastore;

import com.alexandermilne.mapBackend.profile.UserProfile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserDao {

    int insertUserProfile(UUID id, UserProfile person);
     /**
    default int insertPerson(Person person) {
        UUID id = UUID.randomUUID();
        return  insertPerson(id, person);
   }
     **/
    List<UserProfile> getUserProfiles();

    int uploadUserProfileImage(UUID userProfileId, MultipartFile file);
    int uploadVideoToAws(UUID userProfileId, MultipartFile file);


    //List<Person> selectAllPeople();
/*
    Optional<Person> selectPersonById(UUID id);

    int deletePersonById(UUID id);

    int updatePersonById(UUID id, Person person);
 **/
}
