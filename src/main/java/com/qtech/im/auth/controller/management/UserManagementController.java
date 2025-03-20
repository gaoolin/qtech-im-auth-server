package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.service.api.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 13:53:36
 * desc   :  用户管理控制器
 */

@Controller
public class UserManagementController {

    private final IUserService userService;

    public UserManagementController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user-list";
    }
}

