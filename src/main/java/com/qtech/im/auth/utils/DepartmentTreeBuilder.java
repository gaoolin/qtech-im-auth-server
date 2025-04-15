package com.qtech.im.auth.utils;

import com.qtech.im.auth.model.entity.primary.Department;
import com.qtech.im.auth.model.entity.primary.DeptTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/11 11:41:32
 * desc   :
 * 构建树状结构： 通过递归查询，将所有部门数据按父子关系组织成树状结构。
 */
public class DepartmentTreeBuilder {
    public static List<DeptTree> build(List<Department> deptList) {
        if (deptList == null || deptList.isEmpty()) {
            return new ArrayList<>();
        }

        // 创建一个 Map，方便根据 parentId 查找子部门
        Map<Long, DeptTree> idToNodeMap = new HashMap<>();
        List<DeptTree> rootNodes = new ArrayList<>();

        for (Department dept : deptList) {
            DeptTree node = new DeptTree();
            node.setId(dept.getId());
            node.setName(dept.getDeptName());
            node.setParentId(dept.getParentId());
            idToNodeMap.put(node.getId(), node);
        }

        for (DeptTree node : idToNodeMap.values()) {
            if (node.getParentId() == null || node.getParentId().equals(0L)) {
                // 顶层节点
                rootNodes.add(node);
            } else {
                // 非顶层节点，挂到父节点的 children 上
                DeptTree parent = idToNodeMap.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 找不到父节点，打印日志
                    System.out.println("找不到父节点，部门ID: " + node.getId() + ", 父部门ID: " + node.getParentId());
                    rootNodes.add(node); // 如果你希望保留当前节点为根节点
                }
            }
        }

        return rootNodes;
    }
}