package com.qtech.im.auth.controller.management;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 14:29:40
 * desc   :  Controller 示例代码（页面跳转版见上文）
 */

@RestController
@RequestMapping("/api")
public class ApiMockController {

    @GetMapping("/users")
    public List<Map<String, Object>> getUsers() {
        return Arrays.asList(
                new HashMap<String, Object>() {{
                    put("id", 1);
                    put("username", "admin");
                    put("email", "admin@qtech.com");
                    put("role", "超级管理员");
                }},
                new HashMap<String, Object>() {{
                    put("id", 2);
                    put("username", "testuser");
                    put("email", "test@qtech.com");
                    put("role", "普通用户");
                }}
        );
    }

    @GetMapping("/roles")
    public List<Map<String, Object>> getRoles() {
        return Arrays.asList(
                new HashMap<String, Object>() {{
                    put("id", 1);
                    put("roleName", "超级管理员");
                }},
                new HashMap<String, Object>() {{
                    put("id", 2);
                    put("roleName", "普通用户");
                }}
        );
    }

    @GetMapping("/permissions")
    public List<Map<String, Object>> getPermissions() {
        return Arrays.asList(
                new HashMap<String, Object>() {{
                    put("id", 1);
                    put("permissionName", "查看用户");
                }},
                new HashMap<String, Object>() {{
                    put("id", 2);
                    put("permissionName", "编辑用户");
                }}
        );
    }
}