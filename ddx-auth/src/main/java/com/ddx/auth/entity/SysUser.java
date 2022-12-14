package com.ddx.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SysUser
 * @Description: 用户信息表
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private String userId;

    /**
     * 用户名 账户名
     */
    @TableField("username")
    private String username;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 性别：1-男 2-女
     */
    @TableField("gender")
    private String gender;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 联系方式
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 用户状态：1-正常 0-禁用
     */
    @TableField("status")
    private String status;

    /**
     * 默认登入服务
     */
    @TableField("login_service")
    private String loginService;
    /**
     * 用户邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 登入错误次数
     */
    @TableField("error_count")
    private Integer errorCount;

    /**
     * 用户认证类型 bearer类型平台管理端认证 basic 用户客户端认证
     */
    @TableField("authorization_type ")
    private String authorizationType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建者ID
     */
    @TableField(value = "create_id",fill = FieldFill.INSERT)
    private Long createId;

    /**
     * 更新者ID
     */
    @TableField(value = "update_id",fill = FieldFill.INSERT_UPDATE)
    private Long updateId;


}
