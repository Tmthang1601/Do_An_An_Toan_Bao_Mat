package com.bookshopweb.utils;

import com.bookshopweb.beans.CartItem;
import com.bookshopweb.beans.OrderItem;
import com.bookshopweb.beans.ProductCheckItem;

import java.util.List;
import java.util.stream.Collectors;

public class ModelMapping {
    public static ProductCheckItem convertProductCheckItem(CartItem cartItem) {
        ProductCheckItem productCheckItem = new ProductCheckItem();
        productCheckItem.setId(cartItem.getProduct().getId());
        productCheckItem.setPrice(cartItem.getProduct().getPrice());
        productCheckItem.setQuantity(cartItem.getQuantity());
        return productCheckItem;
    }

    public static ProductCheckItem convertProductCheckItem(OrderItem orderItem) {
        ProductCheckItem productCheckItem = new ProductCheckItem();
        productCheckItem.setId(orderItem.getProduct().getId());
        productCheckItem.setPrice(orderItem.getPrice());
        productCheckItem.setQuantity(orderItem.getQuantity());
        return productCheckItem;
    }

    public static List<ProductCheckItem> convertListProductCheckItemFromCartItem(List<CartItem> cartItems){
        return cartItems.stream().map(ModelMapping::convertProductCheckItem).collect(Collectors.toList());
    }

    public static List<ProductCheckItem> convertListProductCheckItemFromOrderItem(List<OrderItem> orderItems){
        return orderItems.stream().map(ModelMapping::convertProductCheckItem).collect(Collectors.toList());
    }
}
