package com.qtech.im.auth.controller.api;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.service.management.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/20 11:23:54
 * desc   :
 */

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth/roles")
public class RoleController {

    @Autowired
    private final IRoleService roleService;

    @GetMapping("/")
    private String getAllRoles(Model model) {
        List<Role> allRoles = roleService.findAllRoles();
        model.addAttribute("roles", allRoles);
        return "role-list";
    }
}
