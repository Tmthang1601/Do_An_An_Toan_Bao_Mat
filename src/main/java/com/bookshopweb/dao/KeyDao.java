package com.bookshopweb.dao;

import com.bookshopweb.beans.Key;
import com.bookshopweb.beans.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Key.class)
public interface KeyDao extends DAO<Key> {
    @Override
    @SqlUpdate("INSERT INTO `key` VALUES (default, :userId, :publicKey, :createAt, :expirationAt)")
    @GetGeneratedKeys("id")
    long insert(@BindBean Key key);

    @Override
    @SqlUpdate("UPDATE `key` SET userId = :userId, publicKey = :publicKey, expirationAt = :expirationAt, " +
            "createAt = :createAt " +
            "WHERE id = :id")
    void update(@BindBean Key key);

    @Override
    @SqlUpdate("DELETE FROM `key` WHERE id = :id")
    void delete(@Bind("id") long id);

    @Override
    @SqlQuery("SELECT * FROM `key` WHERE id = :id")
    Optional<Key> getById(@Bind("id") long id);

    @Override
    @SqlQuery("SELECT * FROM `key`")
    List<Key> getAll();

    @Override
    @SqlQuery("SELECT * FROM key LIMIT :limit OFFSET :offset")
    List<Key> getPart(@Bind("limit") int limit, @Bind("offset") int offset);

    @Override
    @SqlQuery("SELECT * FROM key ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
    List<Key> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
                              @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);

    @SqlQuery("SELECT * FROM `key` WHERE userId = :userId AND createdAt < :createAt AND expirationAt > :createAt")
    Optional<Key> getKeyByUserId(@Bind("userId") long userId, @Bind("createAt") LocalDateTime createAt);

    @SqlQuery("SELECT * FROM `key` WHERE userId = :userId")
    Optional<Key> getByUserId(@Bind("userId") long userId);
}
