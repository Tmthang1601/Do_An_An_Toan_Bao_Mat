package com.bookshopweb.service;

import com.bookshopweb.beans.Key;
import com.bookshopweb.dao.KeyDao;

public class KeyService extends Service<Key, KeyDao> implements KeyDao {
    public KeyService() {
        super(KeyDao.class);
    }
}
