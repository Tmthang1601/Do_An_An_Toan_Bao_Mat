package com.bookshopweb.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class SendMail {
    public static String sendMail(String received, String content){
        final String host = "smtp.gmail.com";
        final String port = "587";
        final String username = "20130203@st.hcmuaf.edu.vn";  // Thay bằng email của bạn
        final String password = "zyqc shbm dolw oyyk";  // Thay bằng mật khẩu hoặc App Password của bạn

        // Thông tin email gửi và nhận
        final String toEmail = received; // Thay bằng email người nhận
        final String subject = "Your private key";
        final String body = "This is your private key "+content;

        // Cấu hình các thuộc tính
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Xác thực tài khoản
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Tạo email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Gửi email
            Transport.send(message);

            return "Email sent successfully!";
        } catch (MessagingException e) {
            return e.getMessage();
        }
    }
}
