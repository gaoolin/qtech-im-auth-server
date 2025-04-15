package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.model.dto.management.DeptTreeNodeDTO;
import com.qtech.im.auth.model.entity.primary.DeptTree;
import com.qtech.im.auth.service.management.IDepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/11 13:57:45
 * desc   :
 */

@SpringBootTest
class AuthDeptsControllerTest {
    @Autowired
    private IDepartmentService departmentService;

    @Test
    void testBuildDepartmentTree() {
        List<DeptTreeNodeDTO> deptTree = departmentService.getDeptTree();
        printDeptTree(deptTree, 0);
    }

    private void printDeptTree(List<DeptTreeNodeDTO> tree, int level) {
        if (tree == null) return;
        String prefix = "  ".repeat(level);
        for (DeptTreeNodeDTO node : tree) {
            System.out.println(prefix + "- " + node.getName());
            printDeptTree(node.getChildren(), level + 1);
        }
    }
}