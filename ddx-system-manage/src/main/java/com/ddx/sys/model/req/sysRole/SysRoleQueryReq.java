package com.ddx.sys.model.req.sysRole;

import com.ddx.basis.model.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SysRoleQueryReq
 * @Description: 角色表查询入参体
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysRoleQueryReq", description="角色表查询入参体")
public class SysRoleQueryReq extends PageReq {

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色状态：0-正常；1-停用")
    private String status;

}
