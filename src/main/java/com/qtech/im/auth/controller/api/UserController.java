package com.qtech.im.auth.controller.api;

import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.model.Permission;
import com.qtech.im.auth.model.Role;
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

    @GetMapping("/{employeeId}/roles")
    public Result<Set<Role>> getUserRoles(@PathVariable String employeeId) {
        return Result.success(userService.getUserRoles(employeeId));
    }

    @GetMapping("/{employeeId}/permissions")
    public Result<Set<Permission>> getUserPermissions(@PathVariable String employeeId) {
        return Result.success(userService.getUserPermissions(employeeId));
    }

    @PostMapping("/{employeeId}/roles/{roleId}")
    public Result<Void> addRoleToUser(@PathVariable String employeeId, @PathVariable Long roleId) {
        userService.addRoleToUser(employeeId, roleId);
        return Result.success();
    }

    @DeleteMapping("/{employeeId}/roles/{roleId}")
    public Result<Void> removeRoleFromUser(@PathVariable String employeeId, @PathVariable Long roleId) {
        userService.removeRoleFromUser(employeeId, roleId);
        return Result.success();
    }

    @PostMapping("/{employeeId}/permissions/{permissionId}")
    public Result<Void> addPermissionToUser(@PathVariable String employeeId, @PathVariable Long permissionId) {
        userService.addPermissionToUser(employeeId, permissionId);
        return Result.success();
    }

    @DeleteMapping("/{employeeId}/permissions/{permissionId}")
    public Result<Void> removePermissionFromUser(@PathVariable String employeeId, @PathVariable Long permissionId) {
        userService.removePermissionFromUser(employeeId, permissionId);
        return Result.success();
    }

    @DeleteMapping("/{employeeId}/roles")
    public Result<Void> removeAllRolesFromUser(@PathVariable String employeeId) {
        userService.removeAllRolesFromUser(employeeId);
        return Result.success();
    }

    @DeleteMapping("/{employeeId}/permissions")
    public Result<Void> removeAllPermissionsFromUser(@PathVariable String employeeId) {
        userService.removeAllPermissionsFromUser(employeeId);
        return Result.success();
    }
}
