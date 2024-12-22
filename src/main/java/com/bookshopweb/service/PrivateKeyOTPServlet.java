package com.bookshopweb.service;

import com.bookshopweb.beans.User;
import com.bookshopweb.utils.OTPUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;
@WebServlet("/generate-private-key-otp")
public class PrivateKeyOTPServlet extends HttpServlet {
    private final OTPUtils otpUtils = new OTPUtils();
    private final UserService userService = new UserService(); // To get user email

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        try {
            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().print("{\"status\": \"error\", \"message\": \"Vui lòng đăng nhập\"}");
                return;
            }

            Optional<User> user = userService.getById(userId);
            if (user.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().print("{\"status\": \"error\", \"message\": \"Người dùng không tồn tại\"}");
                return;
            }

            // Rest of your existing code...
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}