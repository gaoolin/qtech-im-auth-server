package com.qtech.im.auth.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/26 16:44:31
 * desc   :
 */

@Data
@Entity
@Table(name = "IM_AUTH_DEPT")
public class Department extends BaseModel {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "ANCESTORS")
    private String ancestors;
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Column(name = "ORDER_NUM")
    private Integer orderNum;
    @Column(name = "LEADER")
    private String leader;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DEL_FLAG")
    private String delFlag;
}
