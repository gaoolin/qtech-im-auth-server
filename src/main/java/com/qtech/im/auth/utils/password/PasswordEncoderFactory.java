package com.qtech.im.auth.utils.password;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 13:34:31
 * desc   :  使用工厂模式
 */

public class PasswordEncoderFactory {
    public static PasswordEncoder getEncoder(EncoderAlgorithm algorithm) {
        switch (algorithm) {
            case SHA_256:
                return new SHA256Encoder();
            case MD5:
                return new MD5Encoder();
            case BCrypt:
                return new BCryptEncoder();
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm.getAlgorithm());
        }
    }
}