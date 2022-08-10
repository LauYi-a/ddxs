package com.ddx.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID编码")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "用户名 账户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "性别：1-男 2-女")
    @TableField("gender")
    private String gender;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "用户头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "联系方式")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "用户状态：1-正常 0-禁用")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "默认登入服务")
    @TableField("login_service")
    private String loginService;

    @ApiModelProperty(value = "登入错误次数")
    @TableField("error_count")
    private Integer errorCount;

    @ApiModelProperty(value = "用户邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者ID")
    @TableField(value = "create_id",fill = FieldFill.INSERT)
    private Long createId;

    @ApiModelProperty(value = "更新者ID")
    @TableField(value = "update_id",fill = FieldFill.INSERT_UPDATE)
    private Long updateId;
}
