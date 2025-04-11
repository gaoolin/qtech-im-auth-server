package com.qtech.im.auth.model.primary;

import com.qtech.im.auth.model.web.BaseModel;
import jakarta.persistence.*;
import lombok.*;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/26 17:00:12
 * desc   :
 */

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_SYSTEM")
public class System extends BaseModel {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SYS_NAME")
    private String sysName;
}
