package com.ddx.sys.dto.resp.sysPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ClassName: PermissionResp
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年09月21日 20:28
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="PermissionResp", description="所有权限")
public class PermissionResp {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "URL权限标识")
    private String url;

    @ApiModelProperty(value = "服务模块")
    private String serviceModule;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者ID")
    private Long createId;

    @ApiModelProperty(value = "更新者ID")
    private Long updateId;
}
