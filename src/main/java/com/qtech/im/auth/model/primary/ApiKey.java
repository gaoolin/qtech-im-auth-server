package com.qtech.im.auth.model.primary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:05:34
 * desc   :
 */
@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_API_KEY")
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(length = 255)
    private String apiKey;
}