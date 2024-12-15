package com.bookshopweb.utils;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class HashingUtils {
    public static String hash(String s) {
        String hashed = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            BigInteger bi = new BigInteger(1, digest);
            hashed = String.format("%0" + (digest.length << 1) + "X", bi);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashed;
    }

    public static String hashObjectList(List<? extends Serializable> objectList) {
        String hashed = "";
        try {
            // Serialize the list of objects
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            for (Serializable obj : objectList) {
                if (obj != null) {
                    objectOutputStream.writeObject(obj);
                }
            }

            objectOutputStream.flush();
            byte[] objectBytes = byteArrayOutputStream.toByteArray();

            // Hash the serialized bytes
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(objectBytes);
            BigInteger bi = new BigInteger(1, digest);
            hashed = String.format("%0" + (digest.length << 1) + "X", bi);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashed;
    }
}
