package com.bookshopweb.service;

import com.bookshopweb.beans.Key;
import com.bookshopweb.dao.KeyDao;
import com.bookshopweb.dao.UserDAO;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.time.LocalDateTime;
import java.util.Optional;

public class KeyService extends Service<Key, KeyDao> implements KeyDao {
    public Optional<Key> getByUserId(long userId) {
        try (Handle handle = Jdbi.create("your_jdbc_connection_url", "username", "password").open()) {
            KeyDao keyDao = handle.attach(KeyDao.class);
            return keyDao.getByUserId(userId);
        }
    }
    public KeyService() {
        super(KeyDao.class);
    }

    public long insert(Key key) {
        try (Handle handle = Jdbi.create("your_jdbc_connection_url", "username", "password").open()) {
            KeyDao keyDao = handle.attach(KeyDao.class);
            return keyDao.insert(key);
        }
    }

    @Override
    public Optional<Key> getKeyByUserId(long userId, LocalDateTime createAt) {
        return jdbi.withExtension(KeyDao.class, dao -> dao.getKeyByUserId(userId, createAt));
    }
}
