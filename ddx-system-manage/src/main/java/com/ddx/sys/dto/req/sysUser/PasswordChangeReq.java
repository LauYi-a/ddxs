package com.ddx.sys.dto.req.sysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: PasswordChangeReq
 * @Description: 修改用户密码
 * @Author: YI.LAU
 * @Date: 2022年05月20日  0020
 * @Version: 1.0
 */
@Data
@ApiModel(value="PasswordChangeReq", description="修改用户密码")
public class PasswordChangeReq {

    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long id;

    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty(value = "确认新密码")
    @NotBlank(message = "确认新密码不能为空")
    private String newPasswordYes;
}
