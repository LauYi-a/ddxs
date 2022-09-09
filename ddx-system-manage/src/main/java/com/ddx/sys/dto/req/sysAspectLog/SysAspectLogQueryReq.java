package com.ddx.sys.dto.req.sysAspectLog;

import com.ddx.basis.dto.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SysAspectLogQueryReq
 * @Description: 切面日志查询入参体
 * @author YI.LAU
 * @since 2022-04-29
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysAspectLogQueryReq", description="切面日志查询入参体")
public class SysAspectLogQueryReq extends PageReq {

    @ApiModelProperty(value = "全局流水号")
    private String serialNumber;

    @ApiModelProperty(value = "请求接口")
    private String url;

    @ApiModelProperty(value = "请求用户ID")
    private String userId;

}
