package com.qtech.im.auth.service.api.impl;

import com.qtech.im.auth.common.JwtTokenProvider;
import com.qtech.im.auth.model.dto.GenerateClientTokenRequest;
import com.qtech.im.auth.model.dto.GenerateUserTokenRequest;
import com.qtech.im.auth.model.dto.TokenResponse;
import com.qtech.im.auth.exception.biz.TokenGenerationException;
import com.qtech.im.auth.service.api.ITokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/21 09:00:49
 * desc   :  该类作为业务服务层，封装对 JwtTokenProvider 的调用，并结合业务逻辑进行统一处理
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponse generateUserToken(GenerateUserTokenRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("GenerateUserTokenRequest cannot be null");
        }
        String employeeId = request.getEmployeeId();
        String systemName = request.getSystemName();
        String clientId = request.getClientId();
        if (employeeId == null || systemName == null || clientId == null) {
            log.info(">>>>> Generating user token for employeeId={}, system={}, clientId={}", employeeId, systemName, clientId);
            return null;
        }

        String token = jwtTokenProvider.generateTokenForUser(request);

        if (token == null) {
            throw new TokenGenerationException("Token generation failed for user: " + request.getEmployeeId());
        }

        return new TokenResponse(token);
    }

    @Override
    public TokenResponse generateClientToken(GenerateClientTokenRequest request) {
        log.info(">>>>> Generating client token for clientId={}", request.getClientId());

        String token = jwtTokenProvider.generateTokenForClient(request.getClientId());

        if (token == null) {
            throw new TokenGenerationException("Token generation failed for client: " + request.getClientId());
        }

        return new TokenResponse(token);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return jwtTokenProvider.getUsernameFromToken(token);
    }

    @Override
    public String getClientIdFromToken(String token) {
        return jwtTokenProvider.getClientIdFromToken(token);
    }
}