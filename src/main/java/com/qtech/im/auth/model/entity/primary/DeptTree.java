package com.qtech.im.auth.model.entity.primary;

import com.qtech.im.auth.utils.TreeNode;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/11 13:48:12
 * desc   :  部门信息结构树
 * DeptTree 类应该实现 TreeNode<DeptTree>，这样它可以正常返回一个 List<DeptTree> 类型的子节点。
 */

@Data
@ToString(exclude = "children") // 递归打印所有字段，从而陷入死循环
public class DeptTree implements TreeNode<DeptTree> {
    private Long id;
    private String name;
    private Long parentId;
    private List<DeptTree> children = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    @Override
    public List<DeptTree> getChildren() {
        return children;
    }

    public void setChildren(List<DeptTree> children) {
        this.children = children;
    }
}