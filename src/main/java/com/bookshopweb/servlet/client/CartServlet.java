package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.*;
import com.bookshopweb.dto.ErrorMessage;
import com.bookshopweb.dto.OrderRequest;
import com.bookshopweb.dto.SuccessMessage;
import com.bookshopweb.service.*;
import com.bookshopweb.utils.HashingUtils;
import com.bookshopweb.utils.JsonUtils;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "CartServlet", value = "/cart")
public class CartServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final OrderItemService orderItemService = new OrderItemService();
    private final CartService cartService = new CartService();
    private final CartItemService cartItemService = new CartItemService();
    private OrderVerificationService orderVerificationService = new OrderVerificationService();



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("currentUser");
        long userId = user.getId();
        Optional<Cart> optionalCart = cartService.getByUserId(userId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<CartItem> cartItems = cartItemService.getByCartId(cart.getId());
            String hash = HashingUtils.hashObjectList(cartItems);
            System.out.println(hash);
            request.getSession().setAttribute("hashCart", hash);
            request.setAttribute("hashCart", "HashCode của bạn là " + hash);
        }

        request.getRequestDispatcher("/WEB-INF/views/cartView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Lấy đối tượng orderRequest từ JSON trong request
        OrderRequest orderRequest = JsonUtils.get(request, OrderRequest.class);
        Cookie[] cookies = request.getCookies();

        // Khai báo biến để lưu trữ giá trị của cookie 'signature'
        String signatureValue = null;

        // Kiểm tra nếu cookies không rỗng
        if (cookies != null) {
            // Lặp qua tất cả cookie để tìm cookie có tên 'signature'
            for (Cookie cookie : cookies) {
                if ("signature".equals(cookie.getName())) {
                    signatureValue = cookie.getValue();
                    break;
                }
            }
        }

        // In ra giá trị của cookie 'signature' (hoặc xử lý nếu cần)
        if (signatureValue != null) {
            System.out.println("Giá trị signature từ cookie: " + signatureValue);
        } else {
            System.out.println("Không tìm thấy cookie 'signature'.");
        }

        // Tạo order
        Order order = new Order(
                0L,
                orderRequest.getUserId(),
                1,
                orderRequest.getDeliveryMethod(),
                orderRequest.getDeliveryPrice(),
                LocalDateTime.now(),
                null
        );
        long orderId = Protector.of(() -> orderService.insert(order)).get(0L);



        String successMessage = "Đã đặt hàng và tạo đơn hàng thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new SuccessMessage(200, successMessage),
                HttpServletResponse.SC_OK);
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new ErrorMessage(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        if (orderId > 0L) {
            request.setAttribute("hashCart", "");
            List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(orderItemRequest -> new OrderItem(
                    0L,
                    orderId,
                    orderItemRequest.getProductId(),
                    orderItemRequest.getPrice(),
                    orderItemRequest.getDiscount(),
                    orderItemRequest.getQuantity(),
                    LocalDateTime.now(),
                    null
            )).collect(Collectors.toList());


            String hashcode = (String) request.getSession().getAttribute("hashCart");

            // Lưu vào bảng order_verification
            Order_verification orderVerification = new Order_verification(
                    0L, //
                    orderId,
                    hashcode,
                    signatureValue,
                    LocalDateTime.now()
            );

            orderVerificationService.insert(orderVerification);


            Protector.of(() -> {
                        orderItemService.bulkInsert(orderItems);
                        cartService.delete(orderRequest.getCartId());
                    })
                    .done(r -> {doneFunction.run();
                    })
                    .fail(e -> failFunction.run());

        } else {
            failFunction.run();
        }

    }
}
