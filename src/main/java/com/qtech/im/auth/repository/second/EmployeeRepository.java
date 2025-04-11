package com.qtech.im.auth.repository.second;

import com.qtech.im.auth.model.second.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/10 08:50:19
 * desc   :
 */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT DISTINCT e.orgFullPath FROM Employee e WHERE e.orgFullPath IS NOT NULL")
    List<String> findDistinctOrgFullPaths();
}