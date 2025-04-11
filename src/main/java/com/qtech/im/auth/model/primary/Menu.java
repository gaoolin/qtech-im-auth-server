package com.qtech.im.auth.model.primary;

import com.qtech.im.auth.model.web.BaseModel;
import com.qtech.im.auth.utils.MenuType;
import com.qtech.im.auth.utils.MenuTypeConverter;
import com.qtech.im.auth.utils.Status;
import com.qtech.im.auth.utils.StatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/03 10:52:16
 * desc   :
 */

@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_MENU", schema = "IMWEB")
public class Menu extends BaseModel {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "MENU_NAME")
    private String menuName;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "ORDER_NUM")
    private Integer orderNum;
    @Column(name = "PATH")
    private String path;
    @Column(name = "COMPONENT")
    private String component;
    @Column(name = "QUERY")
    private String query;
    @Column(name = "ROUTE_NAME")
    private String routeName;
    @Column(name = "IS_FRAME")
    private Integer isFrame;
    @Column(name = "IS_CACHE")
    private Integer isCache;
    @Column(name = "MENU_TYPE")
    @Convert(converter = MenuTypeConverter.class)
    private MenuType menuType;
    @Column(name = "VISIBLE")
    @Convert(converter = StatusConverter.class)
    private Status visible;
    @Column(name = "PERMS")
    private String perms;
    @Column(name = "ICON")
    private String icon;

}
