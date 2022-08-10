package com.ddx.sys.dto.req.sysResource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * @ClassName: SysResourceEditReq
 * @Description: 系统资源编辑入参体
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysResourceEditReq", description="系统资源编辑入参体")
public class SysResourceEditReq {

    @ApiModelProperty(value = "主键")
    @NotNull(message = "主键不能为空")
    private Long id;

    @ApiModelProperty(value = "根节点")
    @NotNull(message = "根节点不能为空")
    private Long rootId;

    @ApiModelProperty(value = "父节点")
    @NotNull(message = "父节点不能为空")
    private Long parentId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "资源名称")
    @NotBlank(message = "资源名称不能为空")
    private String resourceName;

    @ApiModelProperty(value = "资源类型 - 0 页签 1 菜单 2 元素")
    @NotBlank(message = "资源类型不能为空")
    private String resourceType;

    @ApiModelProperty(value = "重定向地址")
    private String redirect;

    @ApiModelProperty(value = "资源路径")
    @NotBlank(message = "资源路径不能为空")
    private String resourceUrl;

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
    @NotNull(message = "是否缓存不能为空")
    private Boolean cache;

    @ApiModelProperty(value = "使用组件")
    @NotBlank(message = "使用组件不能为空")
    private String component;

}
