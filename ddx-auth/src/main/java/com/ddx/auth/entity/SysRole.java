package com.ddx.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SysRole
 * @Description: 角色表
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
@TableName("sys_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @TableField("name")
    private String name;

    /**
     * 角色编码
     */
    @TableField("code")
    private String code;

    /**
     * 角色状态：0-正常；1-停用
     */
    @TableField("status")
    private String status;

    /**
     * 角色类型 0-平台端；1-用户端
     */
    @TableField("role_type")
    private String roleType;

    /**
     * 是否为默认选择角色 0不默认选择 1默认选择
     */
    @TableField("default_select")
    private String defaultSelect;

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
