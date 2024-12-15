package com.bookshopweb.service;

import com.bookshopweb.beans.Order_verification;
import com.bookshopweb.dao.KeyDao;
import com.bookshopweb.dao.OrderVerificationDAO;

public class OrderVerificationService extends Service<Order_verification, OrderVerificationDAO> implements OrderVerificationDAO {
    public OrderVerificationService() {
        super(OrderVerificationDAO.class);
    }
}
