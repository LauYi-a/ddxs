package com.ddx.sys.dto.resp.sysResource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @ClassName: ServiceMenuResp
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年10月13日 16:41
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="MenuTreeListResp", description="资源菜单树列表")
public class MenuTreeListResp {

    @ApiModelProperty(value = "资源菜单主键")
    private String id;

    @ApiModelProperty(value = "排序号")
    @Pattern(regexp ="^[0-9]*[1-9][0-9]*$", message ="排序号只能输入整数")
    private Integer sort;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "资源类型 - 0 页签 1 菜单 2 元素")
    private String resourceType;

    @ApiModelProperty(value = "资源路径")
    private String resourceUrl;

    @ApiModelProperty(value = "重定向路径")
    private String redirect;

    @ApiModelProperty(value = "使用组件")
    private String component;

    @ApiModelProperty(value = "服务模块 sys-系统服务 auth-认证服务")
    private String serviceModule;

    @ApiModelProperty(value = "是否打开缓存 true 缓存菜单 false不缓存菜单")
    private Boolean cache;

    @ApiModelProperty(value = "是否打开显示tabs标签 0 false 显示 1 true 不显示")
    private Boolean hideTabs;

    @ApiModelProperty(value = "是否打开可以关闭tabs标签 0 false 关闭 1 true 不关闭")
    private Boolean hideClose;

    @ApiModelProperty(value = "菜单是否打开隐藏菜单栏 0 false 隐藏 1 true 不隐藏")
    private Boolean hideMenu;

    @ApiModelProperty(value = "资源子菜单")
    private List<MenuTreeListResp> children;
}
