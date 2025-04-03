package com.qtech.im.auth.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qtech.im.auth.utils.DelFlag;
import com.qtech.im.auth.utils.DelFlagConverter;
import com.qtech.im.auth.utils.Status;
import com.qtech.im.auth.utils.StatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
public class BaseModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Convert(converter = StatusConverter.class)
    @Column(name = "STATUS")
    private Status status;
    @Convert(converter = DelFlagConverter.class)
    @Column(name = "DEL_FLAG")
    private DelFlag delFlag;
    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "UPDATE_TIME")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Column(name = "REMARK")
    private String remark;
}
