package com.ddx.sys.dto.req.sysUser;

import com.ddx.basis.dto.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SysUserQueryReq
 * @Description: 用户信息表查询入参体
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysUserQueryReq", description="用户信息表查询入参体")
public class SysUserQueryReq extends PageReq {

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别：1-男 2-女")
    private String gender;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户状态：1-正常 ,2已锁 0-禁用")
    private String status;

    @ApiModelProperty(value = "默认登入服务")
    private String loginService;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

}
