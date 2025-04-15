package com.qtech.im.auth.model.entity.primary;

import com.qtech.im.auth.model.web.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/26 16:44:31
 * desc   :
 */

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_DEPT")
public class Department extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
