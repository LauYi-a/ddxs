package com.ddx.sys.model.resp.sysUser;

import com.ddx.basis.constant.ConstantUtils;
import com.ddx.sys.model.resp.sysResource.TreeMenuAndElAuthResp;
import com.ddx.sys.model.resp.sysRole.SysRoleResp;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: SysUserResp
 * @Description: 用户信息查询返回信息
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="SysUserResp", description="用户信息查询返回信息")
public class SysUserResp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别：1-男 2-女")
    private String gender;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "联系方式")
    private String mobile;

    @ApiModelProperty(value = "用户状态：1-正常 0-禁用")
    private String status;

    @ApiModelProperty(value = "默认登入服务")
    private String loginService;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "登入错误次数")
    private Integer errorCount;

    @ApiModelProperty(value = "用户角色")
    private List<SysRoleResp> roleList;

    @ApiModelProperty(value = "用户资源")
    private TreeMenuAndElAuthResp resource;

    @JsonFormat(pattern = ConstantUtils.DATE_FORMAT_8, timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = ConstantUtils.DATE_FORMAT_8, timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者ID")
    private Long createId;

    @ApiModelProperty(value = "更新者ID")
    private Long updateId;
}
