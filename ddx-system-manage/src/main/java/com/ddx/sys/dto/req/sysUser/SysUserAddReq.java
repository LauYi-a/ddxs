package com.ddx.sys.dto.req.sysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @ClassName: SysUserAddReq
 * @Description: 用户信息表新增入参体
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysUserAddReq", description="用户信息表新增入参体")
public class SysUserAddReq {

    @ApiModelProperty(value = "登入账号")
    @NotBlank (message = "登入账号不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{6,15}$",message = "登入账号由6-15个字母/数字组成")
    private String username;

    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,7}$|^[\\dA-Za-z_]{6,14}$", message = "名称只能输入全中文或字母，最少2个最大7个中文，字母最少6个最大14个")
    private String nickname;

    @ApiModelProperty(value = "性别：1-男 2-女")
    @NotNull(message = "性别不能为空")
    private String gender;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "1\\d{10}",message = "手机号格式错误")
    private String mobile;

    @ApiModelProperty(value = "用户邮箱")
    @Email(message = "邮箱格式错误")
    @NotBlank(message = "用户邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "用户绑定角色ID集合")
    @NotEmpty(message = "用户绑定角色ID集合不能为空")
    private List<Long> roleIds;

    @ApiModelProperty(value = "用户绑定资源ID集合")
    @NotEmpty(message = "用户绑定资源ID集合不能为空")
    private List<Long> resourceIds;
}
