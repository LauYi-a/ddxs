package com.ddx.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SysPermission
 * @Description: 权限表
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
@TableName("sys_permission")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "权限名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "URL权限标识")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "是否需要授权角色 0 授予 1不授予")
    @TableField("is_role")
    private String isRole;

    @ApiModelProperty(value = "服务模块")
    @TableField("service_module")
    private String serviceModule;

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
