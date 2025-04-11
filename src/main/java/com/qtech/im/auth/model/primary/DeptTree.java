package com.qtech.im.auth.model.primary;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/11 13:48:12
 * desc   :  部门信息结构树
 */

@Data
@ToString(exclude = "children") // 递归打印所有字段，从而陷入死循环
public class DeptTree {
    private Long id;
    private String name;
    private Long parentId;

    private List<DeptTree> children = new ArrayList<>();
}