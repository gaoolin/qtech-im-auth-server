package com.qtech.im.auth.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 11:45:04
 * desc   :
 */

@WebMvcTest(PasswordEncryptor.class)
class PasswordEncryptorTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void encodePassword() {
        System.out.println(PasswordEncryptor.hashPassword("admin123"));
    }
}