package com.qtech.im.auth.model.dto;

import com.qtech.im.auth.utils.TreeNode;
import lombok.Data;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/14 11:20:34
 * desc   :  把数据库不需要暴露的 createdAt、updatedAt、status 隐藏起来了，前端拿到的只是业务需要的数据。
 * <p>
 * Controller 层只接收和返回 DTO
 * </p>
 * Service 层负责将实体类转换成 DTO（或者反过来）
 * <p>
 * 使用工具类如 MapStruct、BeanUtils、ModelMapper 等进行 DTO ↔ Entity 转换
 * </p>
 */

@Data
public class DeptDTO implements TreeNode<DeptDTO> {
    private Long id;
    private Long parentId;
    private String deptName;
    private String leader;
    private String phone;
    private String email;
    private String remark;
    private Integer orderNum;

    @Override
    public List<DeptDTO> getChildren() {
        return null;
    }
}
