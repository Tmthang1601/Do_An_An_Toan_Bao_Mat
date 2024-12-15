package com.bookshopweb.service;

import com.bookshopweb.beans.Key;
import com.bookshopweb.dao.KeyDao;
import com.bookshopweb.dao.UserDAO;

import java.time.LocalDateTime;
import java.util.Optional;

public class KeyService extends Service<Key, KeyDao> implements KeyDao {
    public KeyService() {
        super(KeyDao.class);
    }

    @Override
    public Optional<Key> getKeyByUserId(long userId, LocalDateTime createAt) {
        return jdbi.withExtension(KeyDao.class, dao -> dao.getKeyByUserId(userId, createAt));
    }
}
