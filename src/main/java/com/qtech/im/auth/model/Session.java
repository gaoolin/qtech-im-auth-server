package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:07:15
 * desc   :
 */

@Entity
@Table(name = "IM_AUTH_SESSION")
@Data
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(length = 255)
    private String sessionToken;

    private LocalDateTime expiresAt;
}

