package com.ddx.sys.dto.req.sysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: QueryUserInfoReq
 * @Description: 查询用户信息
 * @Author: YI.LAU
 * @Date: 2022年06月06日 17:53
 * @Version: 1.0
 */
@Data
@ApiModel(value="QueryUserInfoReq", description="查询用户信息")
public class QueryUserInfoReq {

    @ApiModelProperty(value = "id")
    @NotNull(message = "查询的用户ID不能为空")
    private Long id;
}
