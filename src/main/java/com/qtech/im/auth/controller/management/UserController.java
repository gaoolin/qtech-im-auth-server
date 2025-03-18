package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.model.User;
import com.qtech.im.auth.service.api.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 13:53:36
 * desc   :  用户管理控制器
 */

@Controller
public class UserController {

    @GetMapping("/users")
    public String userList(Model model) {
        return "user-list";
    }
}
