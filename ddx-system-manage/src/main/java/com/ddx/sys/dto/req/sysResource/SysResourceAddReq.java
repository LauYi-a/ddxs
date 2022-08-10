package com.ddx.sys.dto.req.sysResource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * @ClassName: SysResourceAddReq
 * @Description: 系统资源新增入参体
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysResourceAddReq", description="系统资源新增入参体")
public class SysResourceAddReq {

    @ApiModelProperty(value = "根节点")
    private Long rootId;

    @ApiModelProperty(value = "父节点")
    private Long parentId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "资源名称")
    @NotBlank(message = "资源名称不能为空")
    private String resourceName;

    @ApiModelProperty(value = "资源类型 - 0 页签 1 菜单 2 元素")
    @NotBlank(message = "资源类型 - 0 页签 1 菜单 2 元素不能为空")
    private String resourceType;

    @ApiModelProperty(value = "资源路径")
    @NotBlank(message = "资源路径不能为空")
    private String resourceUrl;

    @ApiModelProperty(value = "重定向地址")
    private String redirect;

    @ApiModelProperty(value = "服务模块")
    @NotBlank(message = "服务模块不能为空")
    private String serviceModule;

    @ApiModelProperty(value = "是否外部链接  Y是 N 不是")
    @NotBlank(message = "是否外部链接标识不能为空")
    private String isExternal;

    @ApiModelProperty(value = "图标")
    @NotBlank(message = "图标不能为空")
    private String icon;

    @ApiModelProperty(value = "是否缓存")
    private Boolean cache;

    @ApiModelProperty(value = "是否显示tabs标签 false 不显示 true 显示")
    private Boolean hideTabs;

    @ApiModelProperty(value = "是否可以关闭tabs标签 false 不关闭 true 关闭")
    private Boolean hideClose;

    @ApiModelProperty(value = "使用组件")
    @NotBlank(message = "使用组件不能为空")
    private String component;

}
