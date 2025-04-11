package com.qtech.im.auth.model.primary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qtech.im.auth.model.web.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:07:15
 * desc   :
 */

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_SESSION")
public class Session extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @Column(name = "SESSION_TOKEN")
    private String sessionToken;

    @Column(name = "EXPIRES_AT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai") // 使用标准时区ID替代GMT+8
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiresAt;

}

