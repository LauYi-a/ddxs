package com.ddx.sys.model.resp.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: RoleKeyValResp
 * @Description: 角色键值返回信息
 * @Author: YI.LAU
 * @Date: 2022年04月04日
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="RoleKeyValResp", description="角色键值返回信息")
public class RoleKeyValResp {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String name;
}
