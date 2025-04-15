package com.qtech.im.auth.controller.api;

import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.model.entity.primary.Permission;
import com.qtech.im.auth.model.entity.primary.Role;
import com.qtech.im.auth.service.management.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/13 16:20:15
 * desc   :  用户管理接口
 */

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth/users")
public class UserController {
    @Autowired
    private final IUserService userService;

    @GetMapping("/{empId}/roles")
    public Result<Set<Role>> getUserRoles(@PathVariable String empId) {
        return Result.success(userService.getUserRoles(empId));
    }

    @GetMapping("/{empId}/permissions")
    public Result<Set<Permission>> getUserPermissions(@PathVariable String empId) {
        return Result.success(userService.getUserPerms(empId));
    }

    @PostMapping("/{empId}/roles/{roleId}")
    public Result<Void> addRoleToUser(@PathVariable String empId, @PathVariable Long roleId) {
        userService.addRoleToUser(empId, roleId);
        return Result.success();
    }

    @DeleteMapping("/{empId}/roles/{roleId}")
    public Result<Void> removeRoleFromUser(@PathVariable String empId, @PathVariable Long roleId) {
        userService.removeRoleFromUser(empId, roleId);
        return Result.success();
    }

    @PostMapping("/{empId}/permissions/{permId}")
    public Result<Void> addPermissionToUser(@PathVariable String empId, @PathVariable Long permId) {
        userService.addPermissionToUser(empId, permId);
        return Result.success();
    }

    @DeleteMapping("/{empId}/permissions/{permId}")
    public Result<Void> removePermFromUser(@PathVariable String empId, @PathVariable Long permId) {
        userService.removePermFromUser(empId, permId);
        return Result.success();
    }

    @DeleteMapping("/{employeeId}/roles")
    public Result<Void> removeAllRolesFromUser(@PathVariable String employeeId) {
        userService.removeAllRolesFromUser(employeeId);
        return Result.success();
    }

    @DeleteMapping("/{employeeId}/permissions")
    public Result<Void> removeAllPermissionsFromUser(@PathVariable String employeeId) {
        userService.removeAllPermsFromUser(employeeId);
        return Result.success();
    }
}
