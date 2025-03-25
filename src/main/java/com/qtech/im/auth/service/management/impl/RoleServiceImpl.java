package com.qtech.im.auth.service.management.impl;

import com.qtech.im.auth.model.Role;
import com.qtech.im.auth.repository.management.RoleRepository;
import com.qtech.im.auth.service.management.IRoleService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 13:56:00
 * desc   :
 */

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setRoleName(roleDetails.getRoleName());
        role.setDescription(roleDetails.getDescription());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public Role getOrCreateRole(String roleName) {
        Role byRoleName = roleRepository.findByRoleName(roleName);
        if (byRoleName == null) {
            Role role = new Role();
            role.setRoleName(roleName);
            return roleRepository.save(role);
        }
        return byRoleName;
    }

    @Override
    public Page<Role> findRolesWithConditions(String keyword, PageRequest pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return roleRepository.findAll((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.like(root.get("roleName"), "%" + keyword + "%"));
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }, pageable);
        }
       return roleRepository.findAll(pageable);
    }

    @Override
    public Page<Role> findAll(PageRequest pageable) {
        if (pageable != null) {
            return roleRepository.findAll(pageable);
        }
        return null;
    }
}
