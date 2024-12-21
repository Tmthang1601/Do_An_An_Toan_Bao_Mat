package com.bookshopweb.service;

import com.bookshopweb.beans.User;
import com.bookshopweb.utils.SendMail;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet("/VerifyOTP")
public class VerifyOTPServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userOtp = request.getParameter("otp");
        String email = request.getParameter("email");
        final KeyService keyService = new KeyService();
        int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        UserService userService = new UserService();
        boolean removeEmail = userService.deleteEmail(user.getEmail());
        boolean emailAdd = userService.insertEmail(user.getEmail(), otp);
        String otpGet = userService.selectOTP(user.getEmail());
        SendMail.sendMail(email, otpGet);
        response.sendRedirect(request.getContextPath() + "/verifyOTPStep2");
    }
}
