package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:06:33
 * desc   :
 */

@Entity
@Table(name = "IM_AUTH_OAUTH_CLIENT")
@Data
public class OauthClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, unique = true)
    private String clientId;

    @Column(length = 255)
    private String clientSecret;

    @Column(length = 255)
    private String grantTypes;

    @Column(length = 255)
    private String redirectUris;

    @Column(length = 255)
    private String scopes;
}