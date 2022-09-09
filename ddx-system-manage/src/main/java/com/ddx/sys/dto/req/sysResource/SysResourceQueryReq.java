package com.ddx.sys.dto.req.sysResource;

import com.ddx.basis.dto.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SysResourceQuotaReq
 * @Description: 系统资源查询入参体
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysResourceQueryReq", description="系统资源查询入参体")
public class SysResourceQueryReq extends PageReq {


    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "资源类型 - 0 页签 1 菜单 2 元素")
    private String resourceType;

    @ApiModelProperty(value = "资源路径")
    private String resourceUrl;

    @ApiModelProperty(value = "服务模块")
    private String serviceModule;

    @ApiModelProperty(value = "使用组件")
    private String component;

    @ApiModelProperty(value = "是否外部链接 Y是 N不是")
    private String isExternal;

}
