package com.bookshopweb.service;

import com.bookshopweb.beans.User;
import com.bookshopweb.dao.UserDAO;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class UserService extends Service<User, UserDAO> implements UserDAO {
    public UserService() {
        super(UserDAO.class);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return jdbi.withExtension(UserDAO.class, dao -> dao.getByUsername(username));
    }

    @Override
    public void changePassword(long userId, String newPassword) {
        jdbi.useExtension(UserDAO.class, dao -> dao.changePassword(userId, newPassword));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return jdbi.withExtension(UserDAO.class, dao -> dao.getByEmail(email));
    }
    public boolean insertEmail(String email, int otp) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO email (email, otp) VALUES (:email, :otp)")
                    .bind("email", email)
                    .bind("otp", otp)
                    .execute();
        });
        return true;
    }
    public boolean deleteEmail(String email) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM email WHERE email = :email")
                    .bind("email", email)
                    .execute();
        });
        return true;
    }
    public String selectOTP(String email) {
        // Sử dụng withHandle để lấy kết quả trả về
        return jdbi.withHandle(handle -> {
            return handle.createQuery("SELECT otp FROM email WHERE email = :email")
                    .bind("email", email)
                    .mapTo(String.class)  // Chuyển kết quả thành String
                    .findOnly();          // Lấy kết quả đầu tiên (trong trường hợp chỉ có 1 email duy nhất)
        });
    }
    public boolean insertPublickey(Integer userId, String publicKey) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimestamp.getTime());
        calendar.add(Calendar.MONTH, 5);
        Timestamp expiryTimestamp = new Timestamp(calendar.getTimeInMillis());
        int countPublic = jdbi.withHandle(handle ->
                handle.createUpdate("insert into keyUser (userId, publicKey, expirationAt) values (:userId, :publicKey, :expiryTimestamp)")
                        .bind("userId", userId)
                        .bind("publicKey", publicKey)
                        .bind("expiryTimestamp", expiryTimestamp )
                        .execute());
        return countPublic > 0;
    }
    public Integer selectPublicKey(Integer userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("select count(*) from keyUser where userId = :userId")
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .one()
        );
    }


    @Override
    public Optional<User> getByPhoneNumber(String phoneNumber) {
        return jdbi.withExtension(UserDAO.class, dao -> dao.getByPhoneNumber(phoneNumber));
    }

    @Override
    public int count() {
        return jdbi.withExtension(UserDAO.class, UserDAO::count);
    }

    public static void main(String[] args) {
        UserService userService = new UserService();
        System.out.println(userService.selectPublicKey(1));
    }
}
