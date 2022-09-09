package com.ddx.sys.dto.resp.sysRole;

import com.ddx.sys.entity.SysPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: SysRoleResp
 * @Description: 角色信息查询返回信息
 * @Author: YI.LAU
 * @Date: 2022年04月04日
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="SysRoleResp", description="角色信息查询返回信息")
public class SysRoleResp {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色状态：0-正常；1-停用")
    private String status;

    @ApiModelProperty(value = "角色权限对象集合")
    private List<SysPermission> rolePremission;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
}
