package com.qtech.im.auth.controller.management;

import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.model.dto.management.DeptDTO;
import com.qtech.im.auth.model.dto.management.DeptTreeNodeDTO;
import com.qtech.im.auth.service.management.IDepartmentService;
import com.qtech.im.auth.utils.web.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/08 10:24:52
 * desc   :
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/depts")
@Validated
public class AuthDeptsController {
    private final IDepartmentService deptService;

    public AuthDeptsController(IDepartmentService deptService) {
        this.deptService = deptService;
    }

    @GetMapping("/list")
    public PageResponse<DeptDTO> list(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String keyword) {
        Sort sort = Sort.by("id").ascending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<DeptDTO> pageResult;
        if (keyword != null && !keyword.isEmpty()) {
            pageResult = deptService.findDeptsWithConditions(keyword, pageable);
        } else {
            pageResult = deptService.getPage(pageable);
        }

        return new PageResponse<>(pageResult);
    }

    @GetMapping("/tree")
    public Result<List<DeptTreeNodeDTO>> tree() {
        return Result.success(deptService.getDeptTree());
    }

    @PostMapping
    public Result<?> create(@Valid @RequestBody DeptDTO dto) {
        deptService.createDept(dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody DeptDTO dto) {
        dto.setId(id);
        deptService.updateDept(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.success();
    }
}