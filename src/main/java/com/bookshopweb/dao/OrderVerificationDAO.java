package com.bookshopweb.dao;

import com.bookshopweb.beans.Key;
import com.bookshopweb.beans.Order_verification;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Order_verification.class)
public interface OrderVerificationDAO extends DAO<Order_verification> {

    @Override
    @SqlUpdate("INSERT INTO `order_verification` VALUES (default, :orderId, :hashcode, :signature, :createAt)")
    @GetGeneratedKeys("id")
    long insert(@BindBean Order_verification orderVerification);

    @Override
    @SqlUpdate("UPDATE `order_verification` SET orderId = :orderId, hashcode = :hashcode, signature = :signature, " +
            "createAt = :createAt " +
            "WHERE id = :id")
    void update(@BindBean Order_verification orderVerification);

    @Override
    @SqlUpdate("DELETE FROM `order_verification` WHERE id = :id")
    void delete(@Bind("id") long id);

    @Override
    @SqlQuery("SELECT * FROM `order_verification` WHERE id = :id")
    Optional<Order_verification> getById(@Bind("id") long id);

    @Override
    @SqlQuery("SELECT * FROM `order_verification`")
    List<Order_verification> getAll();

    @Override
    @SqlQuery("SELECT * FROM order_verification LIMIT :limit OFFSET :offset")
    List<Order_verification> getPart(@Bind("limit") int limit, @Bind("offset") int offset);

    @Override
    @SqlQuery("SELECT * FROM order_verification ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
    List<Order_verification> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
                             @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);

}
