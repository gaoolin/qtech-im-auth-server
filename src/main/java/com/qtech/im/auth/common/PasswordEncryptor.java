package com.qtech.im.auth.common;

import com.qtech.im.auth.utils.password.EncoderAlgorithm;
import com.qtech.im.auth.utils.password.PasswordEncoder;
import com.qtech.im.auth.utils.password.PasswordEncoderFactory;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:33:16
 * desc   :  密码加密工具类
 */

public class PasswordEncryptor {

    private static final EncoderAlgorithm DEFAULT_ALGORITHM = EncoderAlgorithm.BCrypt;

    // 加密密码，支持指定算法
    // 加密密码
    public static String hashPassword(String password) {
        return hashPassword(password, DEFAULT_ALGORITHM);
    }

    // 加密密码，支持指定算法
    public static String hashPassword(String password, EncoderAlgorithm algorithm) {
        PasswordEncoder encoder = PasswordEncoderFactory.getEncoder(algorithm);
        return encoder.encode(password);
    }

    // 验证密码
    public static boolean matches(String rawPassword, String encodedPassword) {
        return matches(rawPassword, encodedPassword, DEFAULT_ALGORITHM);
    }

    // 验证密码，支持指定算法
    public static boolean matches(String rawPassword, String encodedPassword, EncoderAlgorithm algorithm) {
        PasswordEncoder encoder = PasswordEncoderFactory.getEncoder(algorithm);
        return encoder.matches(rawPassword, encodedPassword);
    }
}