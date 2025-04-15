package com.qtech.im.auth.repository.primary.management;

import com.qtech.im.auth.model.entity.primary.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/08 08:41:59
 * desc   :
 */

@Repository
public interface DeptRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    Optional<Department> findByDeptName(String deptName);

    @Query("SELECT d FROM Department d WHERE d.deptName = :deptName AND d.parentId = :parentId")
    Optional<Department> findByDeptNameAndParentId(String deptName, Long parentId);
}
