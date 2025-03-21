package com.qtech.im.auth.service.api;

import com.qtech.im.auth.dto.GenerateUserTokenRequest;
import com.qtech.im.auth.dto.GenerateClientTokenRequest;
import com.qtech.im.auth.dto.TokenResponse;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 08:56:52
 * desc   :
 */

public interface ITokenService {
    TokenResponse generateUserToken(GenerateUserTokenRequest request);
    TokenResponse generateClientToken(GenerateClientTokenRequest request);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    String getClientIdFromToken(String token);
}