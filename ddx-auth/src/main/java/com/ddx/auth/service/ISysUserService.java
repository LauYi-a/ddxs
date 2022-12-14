package com.ddx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ddx.auth.entity.SysUser;
import com.ddx.util.basis.constant.CommonEnumConstant;

/**
 * @ClassName: ISysUserService
 * @Description: 用户信息表 服务类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 修改用户登入错误次数
     * @param user
     * @param loginType
     */
    void updateErrorCount(SysUser user, CommonEnumConstant.LoginType loginType);
}
