package com.qtech.im.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/26 17:00:12
 * desc   :
 */

@Data
@Entity
@Table(name = "IM_AUTH_SYSTEM")
public class System extends BaseModel {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SYS_NAME")
    private String sysName;
    @Column(name = "APP_NAME")
    private String appName;
    @Column(name = "RSRC_NAME")
    private String rsrcName;
    @Column(name = "ACTION_TYPE")
    private String actionType;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DEL_FLAG")
    private String delFlag;
}
