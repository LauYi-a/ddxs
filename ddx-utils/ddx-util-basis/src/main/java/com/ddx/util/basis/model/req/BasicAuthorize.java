package com.ddx.util.basis.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: BasicAuthorize
 * @Description: Basic 方式验证实体
 * @Author: YI.LAU
 * @Date: 2022年12月13日 10:59
 * @Version: 1.0
 */
@Data
@ApiModel(value = "BasicAuthorize" ,description = "Basic 方式验证实体")
public class BasicAuthorize {

    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("邮箱号")
    private String email;
    @ApiModelProperty("手机号或邮箱验证码")
    private String verificationCode;
    @ApiModelProperty("用户账号")
    private String username;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("登入类型")
    private String loginType;
}
