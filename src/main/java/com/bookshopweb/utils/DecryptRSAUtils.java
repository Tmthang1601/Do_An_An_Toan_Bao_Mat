package com.bookshopweb.utils;

import com.bookshopweb.beans.Key;

import javax.crypto.Cipher;
import javax.swing.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DecryptRSA {
    public static String decryptWithPublicKey(String publicKeyPEM, String ciphertextBase64) throws Exception {
        // Convert the Base64 encoded public key to bytes
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

        // Create the public key specification
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

        // Generate the RSA public key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Initialize the cipher for decryption
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        // Decode the Base64 encoded ciphertext
        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertextBase64);

        // Perform decryption
        byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);

        // Return the decrypted plaintext as a string
        return new String(decryptedBytes);
    }
}
