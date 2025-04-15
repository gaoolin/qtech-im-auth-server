package com.qtech.im.auth.model.entity.primary;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 14:44:04
 * desc   :
 */
@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_ROLE_DEPT")
public class RoleDepartment {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    private Role role;
    @ManyToOne
    @JoinColumn(name = "DEPT_ID", referencedColumnName = "ID")
    private Department department;
}
