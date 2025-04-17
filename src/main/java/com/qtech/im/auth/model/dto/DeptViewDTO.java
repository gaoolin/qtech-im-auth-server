package com.qtech.im.auth.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/15 11:48:53
 * desc   :  面向前端视图
 */

@Getter
@Setter
public class DeptViewDTO {
    private Long id;
    private String deptName;
    private String parentDeptName;
    private Integer childrenCount;
}
