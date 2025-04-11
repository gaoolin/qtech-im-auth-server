package com.qtech.im.auth.model.primary;

import com.qtech.im.auth.model.web.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 11:36:29
 * desc   :  岗位实体
 */

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_POST")
public class Post extends BaseModel {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "POST_CODE")
    private String postCode;
    @Column(name = "POST_NAME")
    private String postName;
    @Column(name = "POST_SORT")
    private Integer postSort;
}
