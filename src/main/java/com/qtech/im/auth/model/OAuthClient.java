package com.qtech.im.auth.model;

import com.qtech.im.auth.utils.DelFlag;
import com.qtech.im.auth.utils.DelFlagConverter;
import com.qtech.im.auth.utils.Status;
import com.qtech.im.auth.utils.StatusConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/03/18 10:06:33
 * desc   :
 * <p>
 * 字段说明
 * 字段	示例值	说明
 * client_id	client_a	客户端唯一标识
 * client_name	MES系统客户端	客户端名称（方便查看和标识）
 * system_name	MES	所属系统名
 * client_secret	secret123	客户端密钥（生产中请加密存储）
 * grant_types	client_credentials	授权类型
 * redirect_uris	http://localhost:8080/callback	回调地址，用于授权码模式或跳转
 * scopes	read,write	权限范围（逗号分隔）
 * created_at	SYSTIMESTAMP	创建时间
 */
@Getter
@Setter
@Entity
@Table(name = "IM_AUTH_OAUTH_CLIENT")
public class OAuthClient extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, unique = true)
    private String clientId;

    @Column(length = 100)
    private String clientName; // 新增：第三方系统名称

    @Column(length = 100)
    private String systemName; // 新增：归属系统标识（可用于在 token 中标识来源）

    @Column(length = 255)
    private String clientSecret;

    @Column(length = 255)
    private String grantTypes;

    @Column(length = 255)
    private String redirectUris;

    @Column(length = 255)
    private String scopes;
}
