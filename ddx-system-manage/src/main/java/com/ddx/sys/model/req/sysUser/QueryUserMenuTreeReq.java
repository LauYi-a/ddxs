package com.ddx.sys.model.req.sysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: QueryMenuTreeReq
 * @Description: 查询菜用户单树
 * @Author: YI.LAU
 * @Date: 2022年05月12日  0012
 * @Version: 1.0
 */
@Data
@ApiModel(value="QueryMenuTreeReq", description="查询用户菜单树")
public class QueryUserMenuTreeReq {

    @ApiModelProperty("用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @ApiModelProperty("服务模块")
    @NotBlank(message = "服务模块不能为空")
    private String serviceModule;
}
