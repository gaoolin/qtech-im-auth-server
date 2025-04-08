package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.model.Department;
import com.qtech.im.auth.repository.management.DepartmentRepository;
import com.qtech.im.auth.service.management.IDepartmentService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> getDeptInfo() {
        return departmentRepository.findAll();
    }

    @Override
    public Page<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Override
    public Page<Department> getDeptInfoWithConditions(String keyword, Pageable pageable) {
        return departmentRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("deptName"), "%" + keyword + "%"));
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Override
    public Department createDept(Department dept) {
        return departmentRepository.save(dept);
    }

    @Override
    public void deleteDept(Long id) {
        departmentRepository.deleteById(id);
    }
}
