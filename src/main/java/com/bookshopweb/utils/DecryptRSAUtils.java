package com.bookshopweb.utils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DecryptRSAUtils {
    public static String decryptWithPublicKey(String cipherText, String publicKeyStr) {
        try {


            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // Khởi tạo Cipher cho giải mã với RSA
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            // Giải mã dữ liệu đã mã hóa Base64
            byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
            byte[] decryptedBytes = cipher.doFinal(cipherBytes);

            // Trả về dữ liệu đã giải mã dưới dạng chuỗi
            return new String(decryptedBytes, "UTF-8");
        } catch (IllegalArgumentException e) {
            // Xử lý khi có lỗi trong quá trình giải mã Base64
            System.out.println("Lỗi trong quá trình giải mã Base64: " + e.getMessage());
            return null;  // Hoặc trả về một giá trị khác nếu cần
        } catch (Exception e) {
            // Xử lý các lỗi khác (ví dụ: lỗi trong quá trình khởi tạo khóa công khai, giải mã RSA, ...)
            System.out.println("Lỗi không xác định: " + e.getMessage());
            return null;
        }
    }
}
