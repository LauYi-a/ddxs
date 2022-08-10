package com.ddx.sys.dto.req.sysRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: SysRoleEditReq
 * @Description: 角色表编辑入参体
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysRoleEditReq", description="角色表编辑入参体")
public class SysRoleEditReq {

    @ApiModelProperty(value = "角色ID")
    @NotBlank(message = "角色ID不能为空")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty(value = "角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String code;

    @ApiModelProperty(value = "角色状态：0-正常；1-停用")
    @NotNull(message = "角色状态不能为空")
    private Boolean status;

    @ApiModelProperty(value = "角色资源ID")
    @NotEmpty(message = "角色资源ID不能为空")
    private List<Long> rolePremissionId;
}
