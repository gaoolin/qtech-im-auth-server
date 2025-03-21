package com.qtech.im.auth.utils.password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 13:27:15
 * desc   :  SHA256Encoder ，继承PasswordEncoder抽象类
 */

public class SHA256Encoder implements PasswordEncoder {
    @Override
    public String encode(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not found: SHA-256", e);
        }
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}