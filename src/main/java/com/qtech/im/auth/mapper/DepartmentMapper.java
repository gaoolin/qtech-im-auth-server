package com.qtech.im.auth.mapper;

import com.qtech.im.auth.model.dto.DeptDTO;
import com.qtech.im.auth.model.dto.DeptTreeNodeDTO;
import com.qtech.im.auth.model.dto.DeptViewDTO;
import com.qtech.im.auth.model.entity.primary.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/14 15:07:55
 * desc   :
 */

/**
 * 部门实体类和树形DTO的转换器
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(source = "deptName", target = "name")
    DeptTreeNodeDTO toTreeNodeDTO(Department department);

    Department toEntity(DeptDTO dto);

    DeptDTO toDTO(Department department);
}


