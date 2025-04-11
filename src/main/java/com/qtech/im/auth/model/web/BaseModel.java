package com.qtech.im.auth.model.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qtech.im.auth.utils.DelFlag;
import com.qtech.im.auth.utils.DelFlagConverter;
import com.qtech.im.auth.utils.Status;
import com.qtech.im.auth.utils.StatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/26 15:39:15
 * desc   :
 */

@Data
@MappedSuperclass // 映射到数据库的父类
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseModel extends AuditFields implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Convert(converter = StatusConverter.class)
    @Column(name = "STATUS")
    private Status status;

    @Convert(converter = DelFlagConverter.class)
    @Column(name = "DEL_FLAG")
    private DelFlag delFlag;

    @Column(name = "REMARK")
    private String remark;

    @Transient // 确保 JPA 不会映射这个字段到数据库
    private LocalDateTime startTime;
    @Transient
    private LocalDateTime endTime;

    // 提供默认构造函数
    public BaseModel() {
        super();
        this.startTime = null; // 根据业务需求设置默认值
        this.endTime = null;   // 根据业务需求设置默认值
    }
}