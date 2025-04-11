package com.qtech.im.auth.model.primary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 11:57:29
 * desc   :
 */
@Entity
@Table(name = "IM_AUTH_ROLE_SYSTEM_MENU",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ROLE_ID", "SYS_ID", "MENU_ID"}))
@Getter
@Setter
public class RoleSystemMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYS_ID", referencedColumnName = "ID")
    private System system;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID", referencedColumnName = "ID")
    private Menu menu;
}