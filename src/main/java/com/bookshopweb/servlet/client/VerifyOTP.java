package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Key;
import com.bookshopweb.beans.User;
import com.bookshopweb.service.UserService;
import com.bookshopweb.utils.SendMail;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.*;
import java.time.LocalDateTime;
import java.util.Base64;
@WebServlet(name = "VerifyOTP", value = "/verifyOTPStep2")
public class VerifyOTP extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/verifyOtp.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String otp = req.getParameter("otp");
        HttpSession session = req.getSession();
        session.getAttribute("currentUser");
        User user = (User) session.getAttribute("currentUser");
        UserService userService = new UserService();
        String otpDb = userService.selectOTP(user.getEmail());
        System.out.println(otp + "otp la");
        System.out.println(otpDb + "otp la");

        if (otpDb.equals(otp)) {
            Key key = new Key();
            key.setId(0L);
            key.setUserId(user.getId());
            key.setCreateAt(LocalDateTime.now());
            key.setExpirationAt(LocalDateTime.of(2999, 12, 31, 0, 0, 0));
            key.setExpirationAt(LocalDateTime.now().plusYears(1));

            KeyPairGenerator keyPairGenerator = null;
            try {
                keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(512); // Độ dài key là 2048-bit
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            key.setPublicKey(publicKeyBase64);

            SendMail.sendMail(user.getEmail(), privateKeyBase64);
            resp.sendRedirect("/verifyOTPStep2");
        }else{
            req.getRequestDispatcher("/WEB-INF/views/verifyOtp.jsp").forward(req, resp);
        }
    }
}