package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.model.Department;
import com.qtech.im.auth.service.management.IDepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/08 10:24:52
 * desc   :
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/depts")
public class AuthDeptsController {
    private final IDepartmentService deptService;

    public AuthDeptsController(IDepartmentService deptService) {
        this.deptService = deptService;
    }

    @GetMapping("/list")
    public Page<Department> getDeptInfo(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        if (keyword != null && !keyword.isEmpty()) {
            return deptService.getDeptInfoWithConditions(keyword, pageable);
        }
        return deptService.findAll(pageable);
    }

    @PostMapping(value = "/add", consumes = "application/x-www-form-urlencoded") // 支持表单数据格式
    public Result<?> addDept(@RequestBody MultiValueMap<String, String> formData) {
        Department dept = new Department();
        dept.setDeptName(formData.getFirst("deptName"));
        dept.setLeader(formData.getFirst("leader"));
        dept.setPhone(formData.getFirst("phone"));
        dept.setEmail(formData.getFirst("email"));
        dept.setRemark(formData.getFirst("remark"));
        deptService.createDept(dept);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> removeDept(@PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.success();
    }
}
