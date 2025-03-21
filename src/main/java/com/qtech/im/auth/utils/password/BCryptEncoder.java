package com.qtech.im.auth.utils.password;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 13:29:07
 * desc   :  BCryptEncoder
 */

import org.mindrot.jbcrypt.BCrypt;

public class BCryptEncoder implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
