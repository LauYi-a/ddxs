package com.ddx.sys.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: SysResource
 * @Description: 系统资源
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_resource")
public class SysResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "根节点")
    @TableField("root_id")
    private Long rootId;

    @ApiModelProperty(value = "父节点")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "资源名称")
    @TableField("resource_name")
    private String resourceName;

    @ApiModelProperty(value = "资源类型 - 0 页签 1 菜单 2 元素")
    @TableField("resource_type")
    private String resourceType;

    @ApiModelProperty(value = "资源路径")
    @TableField("resource_url")
    private String resourceUrl;

    @ApiModelProperty(value = "重定向路径")
    @TableField("redirect")
    private String redirect;

    @ApiModelProperty(value = "使用组件")
    @TableField("component")
    private String component;

    @ApiModelProperty(value = "图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "是否缓存 true缓存菜单 false补缓存菜单")
    @TableField("cache")
    private Boolean cache;

    @ApiModelProperty(value = "是否显示tabs标签 false 不显示 true 显示")
    @TableField("hide_tabs")
    private Boolean hideTabs;

    @ApiModelProperty(value = "是否可以关闭tabs标签 false 不关闭 true 关闭")
    @TableField("hide_tabs")
    private Boolean hideClose;

    @ApiModelProperty(value = "菜单是否隐藏菜单栏 false 不隐藏 true 隐藏")
    @TableField("hide_menu")
    private Boolean hideMenu;

    @ApiModelProperty(value = "服务模块 sys-系统服务 auth-认证服务")
    @TableField("service_module")
    private String serviceModule;

    @ApiModelProperty(value = "是否外部链接 Y是 N不是")
    @TableField("is_external")
    private String isExternal;

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
