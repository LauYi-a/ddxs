package com.ddx.sys.dto.req.sysResource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: SysResourceQuotaReq
 * @Description: 系统资源查询入参体
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysResourceQueryReq", description="系统资源查询入参体")
public class SysResourceQueryReq {


    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "资源路径")
    private String resourceUrl;

    @ApiModelProperty(value = "服务模块")
    @NotBlank(message = "服务模块不能为空")
    private String serviceModule;

}
