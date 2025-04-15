package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.mapper.DepartmentMapper;
import com.qtech.im.auth.model.dto.management.DeptDTO;
import com.qtech.im.auth.model.dto.management.DeptTreeNodeDTO;
import com.qtech.im.auth.model.entity.primary.Department;
import com.qtech.im.auth.repository.primary.management.DeptRepository;
import com.qtech.im.auth.service.management.IDepartmentService;
import com.qtech.im.auth.utils.TreeBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

        List<DeptTreeNodeDTO> nodeList = all.stream()
                .map(departmentMapper::toTreeNodeDTO)
                .collect(Collectors.toList());

        return TreeBuilder.buildTree(nodeList, 0L);
    }

    @Override
    public Page<DeptDTO> findDeptsWithConditions(String keyword, PageRequest pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            Page<Department> all = deptRepository.findAll((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.like(root.get("deptName"), "%" + keyword + "%"));
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }, pageable);
            return all.map(departmentMapper::toDTO);
        }
        return deptRepository.findAll(pageable).map(departmentMapper::toDTO);
    }
}
