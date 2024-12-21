package com.bookshopweb.service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
@WebServlet("/verify-private-key-otp")
public class VerifyPrivateKeyOTPServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String userOTP = request.getParameter("otp");
        String storedOTP = (String) session.getAttribute("privateKeyOTP");

        PrintWriter out = response.getWriter();

        if (storedOTP == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"valid\": false, \"message\": \"Không tìm thấy OTP. Vui lòng yêu cầu OTP mới.\"}");
            return;
        }

        if (userOTP != null && userOTP.equals(storedOTP)) {
            session.removeAttribute("privateKeyOTP");
            out.print("{\"valid\": true}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"valid\": false, \"message\": \"OTP không chính xác\"}");
        }
    }
}