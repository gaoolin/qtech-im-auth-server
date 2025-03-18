package com.qtech.im.auth.controller.management;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 14:27:55
 * desc   :
 */

@Controller
public class PermissionController {

    @GetMapping("/permissions")
    public String permissionList(Model model) {
        return "permission-list";
    }
}
