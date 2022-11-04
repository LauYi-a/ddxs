package com.ddx.sys.model.req.sysWhitelistRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: SysWhitelistRequestAddReq
 * @Description: 白名单路由新增入参体
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysWhitelistRequestAddReq", description="白名单路由新增入参体")
public class SysWhitelistRequestAddReq {

    @ApiModelProperty(value = "白名单路由名称")
    @NotBlank(message = "白名单路由名称不能为空")
    private String name;

    @ApiModelProperty(value = "白名单路由")
    @NotBlank(message = "白名单路由不能为空")
    private String url;

    @ApiModelProperty(value = "白名单类型")
    @NotBlank(message = "白名单类型不能为空")
    private String type;

    @ApiModelProperty(value = "服务模块 sys-系统服务 auth-认证服务")
    @NotBlank(message = "服务模块 sys-系统服务 auth-认证服务不能为空")
    private String serviceModule;

}
