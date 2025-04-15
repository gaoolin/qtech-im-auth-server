package com.qtech.im.auth.repository.primary.api.rabc;

import com.qtech.im.auth.model.entity.primary.SystemConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/09 14:22:08
 * desc   :
 */

public class SysConfigSpecification implements Specification<SystemConfig> {

    private final SystemConfig systemConfig;

    public SysConfigSpecification(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }

    @Override
    public Predicate toPredicate(Root<SystemConfig> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (systemConfig.getConfigName() != null) {
            predicates.add(criteriaBuilder.equal(root.get("configName"), systemConfig.getConfigName()));
        }
        if (systemConfig.getConfigType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("configType"), systemConfig.getConfigType()));
        }
        if (systemConfig.getConfigKey() != null) {
            predicates.add(criteriaBuilder.equal(root.get("configKey"), systemConfig.getConfigKey()));
        }
        if (systemConfig.getStartTime() != null && systemConfig.getEndTime() != null) {
            predicates.add(criteriaBuilder.between(root.get("createTime"), systemConfig.getStartTime(), systemConfig.getEndTime()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}