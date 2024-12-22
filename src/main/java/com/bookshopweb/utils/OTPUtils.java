package com.bookshopweb.utils;

import java.security.SecureRandom;

public class OTPUtils {
    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otpLength = 6;
        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}