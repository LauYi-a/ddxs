package com.ddx.sys.model.req.sysRole;

import com.ddx.util.basis.model.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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

    @ApiModelProperty(value = "角色状态：BasisConstant.ROLE_STATUS 字段分组")
    private String status;

    @ApiModelProperty(value = "是否为默认选择角色 BasisConstant.ROLE_DEFAULT_SELECT 字典分组")
    private String defaultSelect;

    @ApiModelProperty(value = "角色类型 BasisConstant.ROLE_TYPE 字典分组")
    private String roleType;

}
