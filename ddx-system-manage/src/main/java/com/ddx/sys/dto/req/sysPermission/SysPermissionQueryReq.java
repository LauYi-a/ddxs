package com.ddx.sys.dto.req.sysPermission;

import com.ddx.common.dto.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SysPermissionQueryReq
 * @Description: 权限表查询入参体
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysPermissionQueryReq", description="权限表查询入参体")
public class SysPermissionQueryReq extends PageReq {

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "URL权限标识")
    private String url;

    @ApiModelProperty(value = "服务模块")
    private String serviceModule;
}
