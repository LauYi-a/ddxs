package com.ddx.sys.dto.req.sysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @ClassName: UpdateUserBasisInfo
 * @Description: 修改用户基础信息
 * @Author: YI.LAU
 * @Date: 2022年06月15日 11:39
 * @Version: 1.0
 */
@Data
@ApiModel(value="UpdateUserBasisInfo", description="修改用户基础信息")
public class UpdateUserBasisInfo {

    @ApiModelProperty(value = "身份Id")
    @NotBlank(message = "身份Id不可为空")
    private String userId;

    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,7}$|^[\\dA-Za-z_]{6,14}$", message = "名称只能输入全中文或字母\n最少2个最大7个中文，字母最少6个最大14个中文")
    private String nickname;

    @ApiModelProperty(value = "性别：1-男 2-女")
    @NotBlank(message = "性别不能为空")
    private String gender;

    @ApiModelProperty(value = "联系方式")
    @NotBlank(message = "联系方式不能为空")
    @Pattern(regexp = "1\\d{10}",message = "手机号格式错误")
    private String mobile;

    @ApiModelProperty(value = "用户邮箱")
    @Email(message = "邮箱格式错误")
    @NotBlank(message = "用户邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "默认登入服务")
    @NotBlank(message = "默认登入服务不能为空")
    private String loginService;
}
