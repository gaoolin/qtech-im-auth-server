package com.qtech.im.auth.repository.api;

import com.qtech.im.auth.model.OAuthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/19 10:36:47
 * desc   :
 */

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClient, String> {
}