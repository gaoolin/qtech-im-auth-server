package com.qtech.im.auth.model.entity.primary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:08:06
 * desc   :
 */
@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_AUDIT_LOG")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(length = 255)
    private String action;

    @Column(length = 50)
    private String ipAddress;

    private LocalDateTime timestamp;
}
