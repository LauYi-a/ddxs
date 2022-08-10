package com.ddx.sys.dto.req.sysPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: SysPermissionEditReq
 * @Description: 权限表编辑入参体
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysPermissionEditReq", description="权限表编辑入参体")
public class SysPermissionEditReq {

    @ApiModelProperty(value = "主键")
    @NotNull(message = "主键不能为空")
    private Long id;

    @ApiModelProperty(value = "权限名称")
    @NotBlank(message = "权限名称不能为空")
    private String name;
    
    @ApiModelProperty(value = "URL权限标识")
    @NotBlank(message = "URL权限标识不能为空")
    private String url;

    @ApiModelProperty(value = "服务模块")
    @NotBlank(message = "服务模块不能为空")
    private String serviceModule;
}
