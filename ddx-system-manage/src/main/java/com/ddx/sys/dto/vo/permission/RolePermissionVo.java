package com.ddx.sys.dto.vo.permission;

import com.ddx.sys.entity.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: SysRolePermissionVo
 * @Description: 角色权限参数
 * @Author: YI.LAU
 * @Date: 2022年05月23日
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="SysRolePermissionVo", description="角色权限参数")
public class RolePermissionVo {

    @ApiModelProperty(value = "权限ID")
    private Long permissionId;

    @ApiModelProperty(value = "权限url")
    private String url;

    @ApiModelProperty(value = "权限名称")
    private String permissionName;

    @ApiModelProperty(value = "角色")
    private List<SysRole> roles;
}
