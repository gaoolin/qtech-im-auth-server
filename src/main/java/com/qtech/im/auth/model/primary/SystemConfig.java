package com.qtech.im.auth.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qtech.im.auth.model.web.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 16:33:31
 * desc   :
 */

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_SYSTEM_CONFIG")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemConfig extends BaseModel {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @jakarta.persistence.ManyToOne
    @JoinColumn(name = "SYS_ID", referencedColumnName = "ID")
    private System system;
    @Column(name = "CONFIG_NAME")
    private String configName;
    @Column(name = "CONFIG_KEY")
    private String configKey;
    @Column(name = "CONFIG_VALUE")
    private String configValue;
    @Column(name = "CONFIG_TYPE")
    private String configType;
}
