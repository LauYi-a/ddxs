package com.ddx.sys.dto.req.sysWhitelistRequest;

import com.ddx.basis.dto.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SysWhitelistRequestQuotaReq
 * @Description: 白名单路由查询入参体
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysWhitelistRequestQuotaReq", description="白名单路由查询入参体")
public class SysWhitelistRequestQueryReq extends PageReq {

    @ApiModelProperty(value = "白名单路由名称")
    private String name;

    @ApiModelProperty(value = "白名单路由")
    private String url;

    @ApiModelProperty(value = "服务模块 sys-系统服务 auth-认证服务")
    private String serviceModule;

}
