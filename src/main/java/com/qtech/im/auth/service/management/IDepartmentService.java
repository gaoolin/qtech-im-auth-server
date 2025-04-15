package com.qtech.im.auth.service.management;

import com.qtech.im.auth.common.Result;
import com.qtech.im.auth.model.dto.management.DeptDTO;
import com.qtech.im.auth.model.dto.management.DeptTreeNodeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/08 08:43:29
 * desc   :
 */
public interface IDepartmentService {
    Page<DeptDTO> getPage(PageRequest pageable);
    DeptDTO createDept(DeptDTO dept);
    DeptDTO updateDept(DeptDTO dept);
    void deleteDept(Long id);
    List<DeptTreeNodeDTO> getDeptTree();

    Page<DeptDTO> findDeptsWithConditions(String keyword, PageRequest pageable);
}