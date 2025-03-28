package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:07:15
 * desc   :
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "IM_AUTH_SESSION")
@Data
public class Session extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "SESSION_TOKEN")
    private String sessionToken;

    @Column(name = "EXPIRES_AT")
    private LocalDateTime expiresAt;

}

