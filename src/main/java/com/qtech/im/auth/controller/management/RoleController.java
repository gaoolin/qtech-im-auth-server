package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.service.api.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 13:54:40
 * desc   :  角色管理控制器
 */

@Controller
public class RoleController {

    @GetMapping("/roles")
    public String roleList(Model model) {
        return "role-list";
    }
}
