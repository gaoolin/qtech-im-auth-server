package com.qtech.im.auth.service.api;

import com.qtech.im.auth.model.OAuthClient;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:28:39
 * desc   :  认证服务
 */


public interface IOAuthClientService {
    OAuthClient findByClientId(String clientId);
}
