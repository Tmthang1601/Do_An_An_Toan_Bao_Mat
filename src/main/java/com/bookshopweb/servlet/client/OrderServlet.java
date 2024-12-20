package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.*;
import com.bookshopweb.dto.OrderResponse;
import com.bookshopweb.service.KeyService;
import com.bookshopweb.service.OrderItemService;
import com.bookshopweb.service.OrderService;
import com.bookshopweb.service.OrderVerificationService;
import com.bookshopweb.utils.DecryptRSAUtils;
import com.bookshopweb.utils.HashingUtils;
import com.bookshopweb.utils.ModelMapping;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "OrderServlet", value = "/order")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final OrderItemService orderItemService = new OrderItemService();
    private final OrderVerificationService orderVerificationService = new OrderVerificationService();
    private final KeyService keyService = new KeyService();
    private static final int ORDERS_PER_PAGE = 3;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("currentUser");

        if (user != null) {
            int totalOrders = orderService.countByUserId(user.getId());

            // Tính tổng số trang (= tổng số order / số sản phẩm trên mỗi trang)
            int totalPages = totalOrders / ORDERS_PER_PAGE;
            if (totalOrders % ORDERS_PER_PAGE != 0) {
                totalPages++;
            }

            // Lấy trang hiện tại, gặp ngoại lệ (chuỗi không phải số, nhỏ hơn 1, lớn hơn tổng số trang) thì gán bằng 1
            String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
            int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);
            if (page < 1 || page > totalPages) {
                page = 1;
            }

            // Tính mốc truy vấn (offset)
            int offset = (page - 1) * ORDERS_PER_PAGE;

            // Lấy danh sách order, lấy với số lượng là ORDERS_PER_PAGE và tính từ mốc offset
            List<Order> orders = Protector.of(() -> orderService.getOrderedPartByUserId(
                    user.getId(), ORDERS_PER_PAGE, offset
            )).get(ArrayList::new);

            List<OrderResponse> orderResponses = new ArrayList<>();

            for (Order order : orders) {
                //tạo biến xác minh
                int authentic = 1;
                String publicKey = null;
                String signature = null;
                String description = null;

                //lấy oder và convert
                List<OrderItem> orderItems = Protector.of(() -> orderItemService.getByOrderId(order.getId())).get(ArrayList::new);
                List<ProductCheckItem> productCheckItems = ModelMapping.convertListProductCheckItemFromOrderItem(orderItems);

                //hash đơn hàng hiện tại lấy được
                String hash = HashingUtils.hashObjectList(productCheckItems);

                //lấy key để xác minh
                Order_verification orderVerification = orderVerificationService.getById(order.getId()).get();
                Optional<Key> key = keyService.getKeyByUserId(user.getId(), order.getCreatedAt());

                //key tồn tại thì set puclicKey
                if(key.isPresent()) {
                    System.out.println(key.get());
                    publicKey = key.get().getPublicKey();
                    signature = orderVerification.getSignature();
                }
                //lấy publicKey để decode và so sánh
                if(publicKey != null && signature != null) {

                    try {
                        description = DecryptRSAUtils.decryptWithPublicKey(signature,publicKey);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (!orderVerification.getHashcode().equals(description)) {
                        authentic = 2;
                    }
                }


                for(ProductCheckItem productCheckItem : productCheckItems) {
                    System.out.println(productCheckItem);
                }


                if(!hash.equalsIgnoreCase(orderVerification.getHashcode())){
                    authentic = 3;
                }

                double total = 0.0;

                for (OrderItem orderItem : orderItems) {
                    if (orderItem.getDiscount() == 0) {
                        total += orderItem.getPrice() * orderItem.getQuantity();
                    } else {
                        total += (orderItem.getPrice() * (100 - orderItem.getDiscount()) / 100) * orderItem.getQuantity();
                    }
                }

                OrderResponse orderResponse = new OrderResponse(
                        order.getId(),
                        order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        check(orderItemService.getProductNamesByOrderId(order.getId())),
                        order.getStatus(),
                        authentic,
                        total + order.getDeliveryPrice());

                orderResponses.add(orderResponse);
            }

            request.setAttribute("totalPages", totalPages);
            request.setAttribute("page", page);
            request.setAttribute("orders", orderResponses);
        }

        request.getRequestDispatcher("/WEB-INF/views/orderView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    private String check(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        }

        return list.get(0) + " và " + (list.size() - 1) + " sản phẩm khác";
    }
}
