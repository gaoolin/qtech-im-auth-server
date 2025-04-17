package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.mapper.DepartmentMapper;
import com.qtech.im.auth.model.dto.DeptDTO;
import com.qtech.im.auth.model.dto.DeptTreeNodeDTO;
import com.qtech.im.auth.model.dto.DeptViewDTO;
import com.qtech.im.auth.model.entity.primary.Department;
import com.qtech.im.auth.repository.primary.management.DeptRepository;
import com.qtech.im.auth.service.management.IDepartmentService;
import com.qtech.im.auth.utils.TreeBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/08 08:43:54
 * desc   :
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements IDepartmentService {
    private final DeptRepository deptRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public void deleteDept(Long id) {
        deptRepository.deleteById(id);
    }

    @Override
    public Page<DeptDTO> getPage(PageRequest pageable) {
        Page<Department> all = deptRepository.findAll(pageable);
        return all.map(departmentMapper::toDTO);
    }

    @Override
    public DeptDTO createDept(DeptDTO dept) {
        return null;
    }

    @Override
    public DeptDTO updateDept(DeptDTO dept) {
        return null;
    }

    @Override
    public List<DeptTreeNodeDTO> getDeptTree() {
        List<Department> all = deptRepository.findAll(Sort.by("parentId", "orderNum"));

        List<DeptTreeNodeDTO> nodeList = all.stream().map(departmentMapper::toTreeNodeDTO).collect(Collectors.toList());

        return TreeBuilder.buildTree(nodeList, 0L);
    }

    @Override
    public Page<DeptDTO> findDeptsWithConditions(String keyword, PageRequest pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            Page<Department> pageableByConditions = getPageableByConditions(keyword, pageable);
            return pageableByConditions.map(departmentMapper::toDTO);
        }
        return deptRepository.findAll(pageable).map(departmentMapper::toDTO);
    }

    @Override
    public Page<DeptViewDTO> findDeptViews(PageRequest pageable) {
        return mapToDeptViewDTO(pageable);
    }

    @Override
    public Page<DeptViewDTO> findDeptViewsWithConditions(String keyword, PageRequest pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return mapToDeptViewDTO(keyword, pageable);
        }
        return null;
    }

    private Page<Department> getPageableByConditions(String keyword, PageRequest pageable) {
        return deptRepository.findAll((root, query, criteriaBuilder) -> {
            if (keyword != null && !keyword.isEmpty()) {
                return criteriaBuilder.or(criteriaBuilder.like(root.get("deptName"), "%" + keyword + "%"), criteriaBuilder.like(root.get("leader"), "%" + keyword + "%"), criteriaBuilder.like(root.get("phone"), "%" + keyword + "%"), criteriaBuilder.like(root.get("email"), "%" + keyword + "%"));
            } else {
                return null;
            }
        }, pageable);
    }

    private Page<Department> getPageableByConditions(PageRequest pageable) {
        return deptRepository.findAll((root, query, criteriaBuilder) -> null, pageable);
    }

    private Page<DeptViewDTO> mapToDeptViewDTO(String keyword, PageRequest pageable) {
        Page<Department> pageableByConditions;
        if (keyword != null && !keyword.isEmpty()) {
            pageableByConditions = getPageableByConditions(keyword, pageable);
        } else {
            pageableByConditions = getPageableByConditions(pageable);
        }

        List<Department> all = deptRepository.findAll();
        // 构建 id -> 部门名 Map，方便查父部门名称
        Map<Long, String> idToNameMap = all.stream().collect(Collectors.toMap(Department::getId, Department::getDeptName));
        // 构建 parentId -> 子部门列表 Map
        Map<Long, List<Department>> parentToChildrenMap = all.stream().collect(Collectors.groupingBy(Department::getParentId));
        return pageableByConditions.map(dept -> {
            DeptViewDTO dto = new DeptViewDTO();
            dto.setId(dept.getId());
            dto.setDeptName(dept.getDeptName());
            dto.setParentDeptName(idToNameMap.get(dept.getParentId()));
            dto.setChildrenCount(parentToChildrenMap.getOrDefault(dept.getId(), new ArrayList<>()).size());
            return dto;
        });
    }

    private Page<DeptViewDTO> mapToDeptViewDTO(PageRequest pageable) {
        return mapToDeptViewDTO(null, pageable);
    }
}
