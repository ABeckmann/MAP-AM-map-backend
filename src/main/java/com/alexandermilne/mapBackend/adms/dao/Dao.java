package com.alexandermilne.mapBackend.adms.dao;

import com.alexandermilne.mapBackend.adms.model.FrontEndModel.AvailableLicensesVM;
import com.alexandermilne.mapBackend.adms.model.FrontEndModel.UserVideoInfo;
import com.alexandermilne.mapBackend.adms.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Dao {

    int insertUser(UUID Id, User person);
     /**
    default int insertPerson(Person person) {
        UUID id = UUID.randomUUID();
        return  insertPerson(id, person);
   }
     **/
    List<User> getAllUsers();
    Optional<User> getUserById(UUID userId);

    int uploadUserImage(UUID userId, MultipartFile file);
    int uploadVideoToAws(UUID userId, MultipartFile file);
    UUID addVideo(UUID userId, String filename, String strStorageLocation);
    int addAvailableLicence(UUID videoId, int price, String regions);

    UserVideoInfo getVideoInfo(UUID userId, String filename);

    Optional<String> getVideoStorageLocation(UUID videoId);

    List<AvailableLicensesVM> getAvailableLicences(UUID videoId);

    void purchaseLicence(UUID purchasingUser, UUID licenceId);
    //List<Person> selectAllPeople();
/*
    Optional<Person> selectPersonById(UUID id);

    int deletePersonById(UUID id);

    int updatePersonById(UUID id, Person person);
 **/
}
