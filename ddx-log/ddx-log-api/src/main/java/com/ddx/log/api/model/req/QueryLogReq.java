package com.ddx.log.api.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: QueryLogReq
 * @Description: 查询日志请求参数
 * @Author: YI.LAU
 * @Date: 2022年11月04日 16:30
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="QueryLogReq", description="查询日志请求参数")
public class QueryLogReq {

    @ApiModelProperty(value = "服务名称")
    @NotBlank(message = "服务名称不能为空")
    private String serviceName;

    @ApiModelProperty(value = "主机IP")
    @NotBlank(message = "主机IP不能为空")
    private String cpIp;

}
