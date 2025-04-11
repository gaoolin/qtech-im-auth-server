package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.model.primary.Department;
import com.qtech.im.auth.model.primary.DeptTree;
import com.qtech.im.auth.repository.primary.management.DeptRepository;
import com.qtech.im.auth.service.management.IDepartmentService;
import com.qtech.im.auth.utils.DelFlag;
import com.qtech.im.auth.utils.DepartmentTreeBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/08 08:43:54
 * desc   :
 */
@Service
@Transactional
public class DepartmentServiceImpl implements IDepartmentService {
    private final DeptRepository deptRepository;

    public DepartmentServiceImpl(DeptRepository deptRepository) {
        this.deptRepository = deptRepository;
    }

    @Override
    public List<Department> getDeptInfo() {
        return deptRepository.findAll();
    }

    @Override
    public Page<Department> findAll(Pageable pageable) {
        return deptRepository.findAll(pageable);
    }

    @Override
    public Page<Department> getDeptInfoWithConditions(String keyword, Pageable pageable) {
        return deptRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("delFlag"), DelFlag.EXISTS));
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("deptName"), "%" + keyword + "%"));
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Override
    public Department createDept(Department dept) {
        return deptRepository.save(dept);
    }

    @Override
    public void deleteDept(Long id) {
        deptRepository.deleteById(id);
    }

    @Override
    public Department updateDept(Department dept) {
        if (dept.getId() != null) {
            return deptRepository.save(dept);
        }
        return null;
    }

    @Override
    public List<DeptTree> getDeptTree() {
        List<Department> allDepts = deptRepository.findAll(Sort.by("orderNum"));
        return DepartmentTreeBuilder.build(allDepts);
    }
}
