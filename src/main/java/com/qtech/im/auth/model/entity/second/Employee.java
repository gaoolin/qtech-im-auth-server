package com.qtech.im.auth.model.entity.second;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/09 17:30:24
 * desc   :
 */
@Getter
@Setter
@Entity
@Table(name = "IM_EMPLOYEE_INFO", schema = "IMBIZ")
public class Employee {

    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BASE_CODE")
    private String baseCode;

    @Column(name = "STAFF_NAME")
    private String staffName;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "ORG_FULL_PATH")
    private String orgFullPath;

    @Column(name = "DEPT_ORG_NAME")
    private String deptOrgName;

    @Column(name = "SYNC_TIME")
    private LocalDateTime syncTime;
}

