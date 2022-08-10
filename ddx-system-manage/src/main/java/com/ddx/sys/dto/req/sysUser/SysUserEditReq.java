package com.ddx.sys.dto.req.sysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @ClassName: SysUserEditReq
 * @Description: 用户信息表编辑入参体
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysUserEditReq", description="用户信息表编辑入参体")
public class SysUserEditReq {

    @ApiModelProperty(value = "id")
    @NotNull(message = "主键不能为空")
    private Long id;

    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,7}$|^[\\dA-Za-z_]{6,14}$", message = "名称只能输入全中文或字母，最少2个最大7个中文，字母最少6个最大14个")
    private String nickname;

    @ApiModelProperty(value = "性别：1-男 2-女")
    @NotNull(message = "性别不能为空")
    private String gender;

    @ApiModelProperty(value = "联系方式")
    @NotBlank(message = "联系方式不能为空")
    @Pattern(regexp = "1\\d{10}",message = "手机号格式错误")
    private String mobile;

    @ApiModelProperty(value = "用户邮箱")
    @Email(message = "邮箱格式错误")
    @NotBlank(message = "用户邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "用户状态：1-正常 0-禁用")
    @NotBlank(message = "用户状态不能为空")
    private String status;

    @ApiModelProperty(value = "默认登入服务")
    @NotBlank(message = "默认登入服务不能为空")
    private String loginService;

    @ApiModelProperty(value = "用户绑定角色ID集合")
    @NotEmpty(message = "用户绑定角色ID集合不能为空")
    private List<Long> roleIds;

    @ApiModelProperty(value = "用户绑定资源ID集合")
    @NotEmpty(message = "用户绑定资源ID集合不能为空")
    private List<Long> resourceIds;
}
