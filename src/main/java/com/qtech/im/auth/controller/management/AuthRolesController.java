package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.service.management.IRoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/25 10:51:24
 * desc   :  认证中心角色控制器
 */

@Controller
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/auth/roles")
public class AuthRolesController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("/list")
    @ResponseBody
    public Page<Role> getRoleInfo(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return roleService.findRolesWithConditions(keyword, pageable);
        }
        return roleService.findAll(pageable);
    }

    @PostMapping("/add")
    @ResponseBody
    private Result<?> addRole(@RequestBody Role role) {
        try {
            roleService.createRole(role);
            return Result.success("添加成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
