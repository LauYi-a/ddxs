package com.ddx.sys.model.req.sysResource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "资源名称")
    @NotBlank(message = "资源名称不能为空")
    private String resourceName;

    @ApiModelProperty(value = "是否缓存")
    @NotNull(message = "是否缓存不能为空")
    private Boolean cache;

    @ApiModelProperty(value = "是否打开显示tabs标签")
    @NotNull(message = "是否显示tabs标签不能为空")
    private Boolean hideTabs;

    @ApiModelProperty(value = "是否打开可以关闭tabs标签")
    @NotNull(message = "是否关闭tabs标签不能为空")
    private Boolean hideClose;

    @ApiModelProperty(value = "菜单是否打开隐藏菜单栏")
    @NotNull(message = "是否隐藏菜单栏不能为空")
    private Boolean hideMenu;

}
