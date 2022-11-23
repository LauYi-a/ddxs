package com.ddx.sys.model.vo.user;

import com.ddx.sys.entity.SysUser;
import com.ddx.sys.model.req.sysUser.SysUserAddReq;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.utils.SerialNumber;
import com.ddx.util.basis.utils.sm3.SM3Digest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @ClassName: UserAddVo
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年08月31日 15:01
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="UserAddVo", description="添加用户信息实体")
public class UserAddVo {

    @ApiModelProperty(value = "登入账号")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别：1-男 2-女")
    private String gender;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户绑定角色ID集合")
    private List<Long> roleIds;

    @ApiModelProperty(value = "用户绑定资源ID集合")
    private List<Long> resourceIds;

    @ApiModelProperty(value = "默认登入服务")
    private String loginService;

    public static SysUser getSysUser(SysUserAddReq sysUserAddReq,String loginService){
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserAddReq,sysUser);
        sysUser.setPassword(SM3Digest.decode(BasisConstant.INIT_PASSWORD+sysUser.getUsername()));
        sysUser.setUserId(SerialNumber.newInstance(BasisConstant.SERIAL_ID, BasisConstant.DATE_FORMAT_5).toString());
        sysUser.setStatus(CommonEnumConstant.Dict.USER_STATUS_1.getDictKey());
        sysUser.setLoginService(loginService);
        return sysUser;
    }
}
