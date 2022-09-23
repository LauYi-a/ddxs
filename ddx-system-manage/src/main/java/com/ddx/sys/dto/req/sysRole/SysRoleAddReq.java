package com.ddx.sys.dto.req.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName: SysRoleAddReq
 * @Description: 角色表新增入参体
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysRoleAddReq", description="角色表新增入参体")
public class SysRoleAddReq {

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty(value = "角色资源ID")
    @NotEmpty(message = "角色资源ID不能为空")
    private List<Long> rolePermissionId;

}
