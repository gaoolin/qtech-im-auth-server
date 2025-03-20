package com.qtech.im.auth.common;

import com.qtech.im.auth.utils.EncoderAlgorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:33:16
 * desc   :  密码加密工具类
 */

public class PasswordEncoderUtils {

    private static final EncoderAlgorithm DEFAULT_ALGORITHM = EncoderAlgorithm.SHA_256;

    // 加密密码
    public static String encodePassword(String password) {
        return encodePassword(password, DEFAULT_ALGORITHM);
    }

    // 加密密码，支持指定算法
    public static String encodePassword(String password, EncoderAlgorithm algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm.getAlgorithm());
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not found: " + algorithm.getAlgorithm(), e);
        }
    }

    // 验证密码
    public static boolean matches(String rawPassword, String encodedPassword) {
        return matches(rawPassword, encodedPassword, DEFAULT_ALGORITHM);
    }

    // 验证密码，支持指定算法
    public static boolean matches(String rawPassword, String encodedPassword, EncoderAlgorithm algorithm) {
        String encodedRawPassword = encodePassword(rawPassword, algorithm);
        return encodedRawPassword.equals(encodedPassword);
    }
}