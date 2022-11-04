package com.ddx.sys.model.req.sysPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PermissionQueryNoPageReq
 * @Description: 不分页权限查询实体
 * @Author: YI.LAU
 * @Date: 2022年09月30日 15:34
 * @Version: 1.0
 */
@Data
@ApiModel(value="PermissionQueryNoPageReq", description="权限表查询入参体")
public class PermissionQueryNoPageReq {

    @ApiModelProperty(value = "是否授予角色权限")
    private String isRole;
}
