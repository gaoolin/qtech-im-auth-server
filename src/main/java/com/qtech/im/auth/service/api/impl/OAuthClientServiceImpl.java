package com.qtech.im.auth.service.api.impl;

import com.qtech.im.auth.model.OAuthClient;
import com.qtech.im.auth.repository.OAuthClientRepository;
import com.qtech.im.auth.service.api.IOAuthClientService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 10:34:14
 * desc   :
 */

@Service
public class OAuthClientServiceImpl implements IOAuthClientService {
    private final OAuthClientRepository oAuthClientRepository;

    public OAuthClientServiceImpl(OAuthClientRepository oAuthClientRepository) {
        this.oAuthClientRepository = oAuthClientRepository;
    }

    @Override
    public OAuthClient findByClientId(String clientId) {
        Optional<OAuthClient> optional = oAuthClientRepository.findById(clientId);
        return optional.orElse(null);
    }
}
