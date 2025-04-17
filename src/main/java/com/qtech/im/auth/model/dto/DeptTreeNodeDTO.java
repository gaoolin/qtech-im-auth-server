package com.qtech.im.auth.model.dto;

import com.qtech.im.auth.utils.TreeNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/14 14:25:48
 * desc   :  泛型参数不能漏！
 */
@Getter
@Setter
public class DeptTreeNodeDTO implements TreeNode<DeptTreeNodeDTO> {
    private Long id;
    private Long parentId;
    private String name;
    private List<DeptTreeNodeDTO> children = new ArrayList<>();
}