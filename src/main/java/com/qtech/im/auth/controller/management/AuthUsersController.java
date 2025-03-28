package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.common.PasswordEncryptor;
import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.common.ResultCode;
import com.qtech.im.auth.model.User;
import com.qtech.im.auth.service.management.IUserService;
import com.qtech.im.auth.utils.BizStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/25 10:51:09
 * desc   :  认证中心用户控制器
 */

@Controller
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/auth/users")
public class AuthUsersController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public String userPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Object tokenObj = session.getAttribute("access_token");
        String accessToken = tokenObj != null ? tokenObj.toString() : "";
        System.out.println(accessToken);
        model.addAttribute("access_token", accessToken);
        return "users";
    }

    @GetMapping("/list")
    @ResponseBody
    public Page<User> getUserInfo(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "employeeId") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return userService.findUsersWithConditions(keyword, keyword, pageable);
        }
        return userService.findAll(pageable);
    }

    @PostMapping("/add")
    @ResponseBody
    private Result<?> addUser(@RequestBody User user) {
        try {
            String password = user.getPwHash();
            String hashPassword = PasswordEncryptor.hashPassword(password);
            user.setPwHash(hashPassword);
            user.setStatus(BizStatus.ACTIVE);
            user.setCreatedAt(LocalDateTime.now());
            userService.createUser(user);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(ResultCode.CUSTOM_ERROR, e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    private Result<?> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            user.setUpdatedAt(LocalDateTime.now());
            userService.updateUser(Long.parseLong(id), user);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(ResultCode.CUSTOM_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    private Result<?> removeUser(@PathVariable String id) {
        try {
            userService.deleteUser(Long.parseLong(id));
            return Result.success();
        } catch (Exception e) {
            return Result.failure(ResultCode.CUSTOM_ERROR, e.getMessage());
        }
    }
}
