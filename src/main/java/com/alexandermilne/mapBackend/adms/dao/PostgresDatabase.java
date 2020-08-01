package com.alexandermilne.mapBackend.adms.dao;

import com.alexandermilne.mapBackend.adms.model.AvailableLicense;
import com.alexandermilne.mapBackend.adms.model.FrontEndModel.AvailableLicensesVM;
import com.alexandermilne.mapBackend.adms.model.FrontEndModel.UserVideoInfo;
import com.alexandermilne.mapBackend.adms.model.User;
import com.alexandermilne.mapBackend.adms.model.UserLicense;
import com.alexandermilne.mapBackend.adms.model.UserVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("PostgresDao")
public class PostgresDatabase implements Dao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgresDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        //ensureBothUsersInDb();
    }

//    private void ensureBothUsersInDb(){
//        UUID userId1 = UUID.fromString("93bf9ae1-2a1f-47b1-83f6-e9fe4ba1710a");
//        UUID userId2 = UUID.fromString("6203c0f4-63d1-4d51-91f5-64de0ee6073d");
//
//
//
//        if (!isUserInDb(userId1)){
//            final String sqlUser1INSERT = "INSERT INTO public.map_user(\n" +
//                    "\t\"Id\", username, userprofileimagelink, \"userMoney\")\n" +
//                    "\tVALUES ('93bf9ae1-2a1f-47b1-83f6-e9fe4ba1710a', 'jamesBond', NULL, 100);";
//        }
//        if (!isUserInDb(userId2)){
//            final String sqlUser1INSERT = "INSERT INTO public.map_user(\n" +
//                    "\t\"Id\", username, userprofileimagelink, \"userMoney\")\n" +
//                    "\tVALUES ('6203c0f4-63d1-4d51-91f5-64de0ee6073d', 'jamesBond', NULL, 100);";
//        }
//
//
//    }
//
//    private Boolean isUserInDb(UUID userId){
//        final String sqlUser1SELECT = "SELECT \"Id\"\n" +
//                "    FROM public.map_user\n" +
//                "    WHERE \"Id\"=?;";
//        String user1Id = jdbcTemplate.queryForObject(sqlUser1SELECT, new Object[]{userId}, (resultSet, i) -> {
//            return resultSet.getString("Id");
//        }
//
//        );
//        return !user1Id.equals(null);
//    }

    @Override
    public int insertUser(UUID id, User person) {
        return 0;
    }

    @Override
    public List<User> getAllUsers() {
        final String sql = "SELECT * FROM public.map_user";

        List<User> users = jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID userId = UUID.fromString(resultSet.getString("Id"));
            User u = new User(
                    userId,
                    resultSet.getString("username"),
                    resultSet.getString("userProfileImageLink"),
                    resultSet.getInt("userMoney")
            );
            u.setUserVideos(getAllUserVideos(userId));
            return u;
        });
        return users;
    }

    public List<UserVideo> getAllUserVideos(UUID userId) {

        final String sqlUserVideos = String.format("SELECT *\n" +
                "\tFROM public.video\n" +
                "\tWHERE \"videoOwnerId\"='%s';", userId);

        List<UserVideo> userVideos = jdbcTemplate.query(sqlUserVideos, (rs, i) -> {
            UUID videoId = UUID.fromString(rs.getString("Id"));
            UserVideo uv = new UserVideo(
                    videoId,
                    UUID.fromString(rs.getString("videoOwnerId")),
                    rs.getString("title"),
                    rs.getString("localStorageLocation")
            );
            uv.setUserLicences(getUserLicencesByVideoId(videoId));
            uv.setAvailableLicense(getAvailableLicensesByVideoId(videoId));
            return uv;
        });
        return userVideos;
    }

    public List<AvailableLicense> getAvailableLicensesByVideoId(UUID videoId) {

        final String sql = String.format("SELECT *\n" +
                "\tFROM public.\"userLicense\"\n" +
                "\tWHERE \"videoId\"='%s';", videoId);

        List<AvailableLicense> qr = jdbcTemplate.query(sql, (rs, j) -> {
            return new AvailableLicense(
                    UUID.fromString(rs.getString("Id")),
                    rs.getInt("price"),
                    rs.getString("region"),
                    UUID.fromString(rs.getString("videoId"))

            );
        });

        return qr;
    }

    public List<UserLicense> getUserLicencesByVideoId(UUID videoId) {

        final String sql = String.format("SELECT *\n" +
                "\tFROM public.\"userLicense\"\n" +
                "\tWHERE \"videoId\"='%s';", videoId);

        List<UserLicense> qr = jdbcTemplate.query(sql, (rs, j) -> {
            return new UserLicense(
                    UUID.fromString(rs.getString("Id")),
                    rs.getInt("price"),
                    rs.getString("region"),
                    UUID.fromString(rs.getString("licenceOwnerId")),
                    UUID.fromString(rs.getString("videoId"))
                    );
        });

        return qr;
    }

    public List<UserLicense> getUserLicences(UUID userId) {

        final String sql = String.format("SELECT *\n" +
                "\tFROM public.\"userLicense\"\n" +
                "\tWHERE \"licenceOwnerId\"='%s';", userId);

        List<UserLicense> qr = jdbcTemplate.query(sql, (rs, j) -> {
            return new UserLicense(
                    UUID.fromString(rs.getString("Id")),
                    rs.getInt("price"),
                    rs.getString("region"),
                    UUID.fromString(rs.getString("licenceOwnerId")),
                    UUID.fromString(rs.getString("videoId"))
                    );
        });

        return qr;
    }


    @Override
    public Optional<User> getUserById(UUID userId) {
        final String sql = "SELECT *" +
                "    FROM public.map_user\n" +
                "    WHERE \"Id\"=?;";


        User u = jdbcTemplate.queryForObject(sql, new Object[]{userId},
                (resultSet, i) -> {
                    UUID uId = UUID.fromString(resultSet.getString("Id"));
                    User tempU = new User(
                            uId,
                            resultSet.getString("username"),
                            resultSet.getString("userProfileImageLink"),
                            resultSet.getInt("userMoney")
                    );
                    tempU.setUserVideos(getAllUserVideos(userId));
                    return tempU;
                }

        );
        return Optional.ofNullable(u);
    }


    @Override
    public int uploadUserImage(UUID userProfileId, MultipartFile file) {
        return 0;
    }

    @Override
    public int uploadVideoToAws(UUID userProfileId, MultipartFile file) {
        return 0;
    }

    @Override
    public UUID addVideo(UUID userProfileId, String filename, String strStorageLocation) {

        UUID vidId= UUID.randomUUID();
        final String sql = String.format("INSERT INTO public.video(\n" +
                "        \"Id\", \"videoOwnerId\", title, \"localStorageLocation\")\n" +
                "        VALUES ('%s', '%s', '%s', '%s');", vidId, userProfileId, filename, strStorageLocation);

        jdbcTemplate.update(sql);

        return vidId;
    }

    @Override
    public int addAvailableLicence(UUID videoId, int price, String region) {

        final String sql = String.format("INSERT INTO public.\"availableLicence\"(\n" +
                "        \"Id\", \"videoId\", price, \"region\")\n" +
                "        VALUES ('%s', '%s', %s, '%s');", UUID.randomUUID(), videoId, price, region);

        jdbcTemplate.update(sql);

        return 0;
    }

    @Override
    public UserVideoInfo getVideoInfo(UUID userProfileId, String filename) {

        return null;
    }

    @Override
    public Optional<String> getVideoStorageLocation(UUID videoId) {
        final String sql = "SELECT \"localStorageLocation\"\n" +
                "\tFROM public.\"video\"\n" +
                "\tWHERE \"Id\"=?;";

        String item = jdbcTemplate.queryForObject(sql, new Object[]{videoId},
                (resultSet, i) -> resultSet.getString("localStorageLocation")
        );
        return Optional.ofNullable(item);
    }


    @Override
    public List<AvailableLicensesVM> getAvailableLicences(UUID videoId) {

        final String sql = String.format(
                "SELECT public.\"availableLicence\".\"Id\", public.\"video\".\"videoOwnerId\", public.\"availableLicence\".\"videoId\", public.\"availableLicence\".price, public.\"availableLicence\".region\n" +
                        "FROM public.\"availableLicence\"\n" +
                        "INNER JOIN public.\"video\" ON public.\"availableLicence\".\"videoId\"=public.\"video\".\"Id\"\n" +
                        "WHERE \"videoId\"='%s';", videoId);

//                "SELECT \"Id\", \"price\", \"region\"\n" +
//                "\tFROM public.\"availableLicence\"\n" +
//                "\tWHERE \"videoId\"='%s';", videoId);

        List<AvailableLicensesVM> availableLicensesVM = jdbcTemplate.query(sql, (rs, i) -> {
            AvailableLicensesVM item = new AvailableLicensesVM(
                    UUID.fromString(rs.getString("Id")),
                    rs.getInt("price"),
                    rs.getString("region"),
                    UUID.fromString(rs.getString("videoOwnerId"))
            );
            return item;
        });
        return availableLicensesVM;
    }

    public Optional<AvailableLicense> getAvailableLicenceById(UUID licenceId) {
        final String sql = "SELECT *" +
                "    FROM public.\"availableLicence\"\n" +
                "    WHERE \"Id\"=?;";


        AvailableLicense u = jdbcTemplate.queryForObject(sql, new Object[]{licenceId},
                (resultSet, i) -> {
                    UUID uId = UUID.fromString(resultSet.getString("Id"));
                    AvailableLicense tempU = new AvailableLicense(
                            uId,
                            resultSet.getInt("price"),
                            resultSet.getString("region"),
                            UUID.fromString(resultSet.getString("videoId"))
                    );
                    return tempU;
                }

        );
        return Optional.ofNullable(u);
    }

    public boolean deleteAvailableLicense(UUID licenceId){
        String sql = "DELETE FROM public.\"availableLicence\" " +
                "WHERE \"Id\"=?;";
        Object[] args = new Object[] {licenceId};
        return jdbcTemplate.update(sql, args) == 1;
    }

    public void createUserLicence(UUID videoId, UUID licenceOwner, int price, String region){
        final String sql = String.format("INSERT INTO public.\"userLicence\"(\n" +
                "        \"Id\", \"videoId\", price, \"region\")\n" +
                "        VALUES ('%s', '%s', %s, '%s');", UUID.randomUUID(), videoId, price, region);

        jdbcTemplate.update(sql);
    }

    @Override
    public void purchaseLicence(UUID purchasingUser, UUID licenceId) {
        Optional<AvailableLicense> availableLicenceOpt = getAvailableLicenceById(licenceId);
        final String sql = String.format("INSERT INTO public.\"availableLicence\"(\n" +
                "        \"Id\", \"videoId\", price, \"region\")\n" +
                "        VALUES ('%s', '%s', %s, '%s');", UUID.randomUUID(), videoId, price, region);
        if (availableLicenceOpt.isPresent()) {
            AvailableLicense availableLicence = availableLicenceOpt.get();
            deleteAvailableLicense(licenceId);
            jdbcTemplate.update(sql);
        }




        //#TODO
    }
}