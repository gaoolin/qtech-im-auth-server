package com.qtech.im.auth.utils;

import lombok.Getter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 16:35:27
 * desc   :
 */

@Getter
public enum EncoderAlgorithm {
    SHA_256("SHA-256"),
    MD5("MD5");

    private final String algorithm;

    EncoderAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}

