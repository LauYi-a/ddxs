package com.ddx.auth.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.auth.entity.SysUser;
import com.ddx.auth.mapper.SysUserMapper;
import com.ddx.auth.service.ISysUserService;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.model.vo.SysParamConfigVo;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @ClassName: SysUserServiceImpl
 * @Description: 用户信息表 服务实现类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private RedisTemplateUtil redisTemplateUtils;

    @Override
    public void updateErrorCount(SysUser user,CommonEnumConstant.LoginType loginType){
        SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(RedisConstant.SYS_PARAM_CONFIG);
        if (user.getErrorCount() >= sysParamConfigVo.getLpec()) {
            user.setStatus(CommonEnumConstant.Dict.USER_STATUS_2.getDictKey());
            String lockedKey = Objects.equals(loginType.getTypeKey(),CommonEnumConstant.LoginType.LOGIN_TYPE_ACCOUNT.getTypeKey())?user.getUsername():
                            Objects.equals(loginType.getTypeKey(),CommonEnumConstant.LoginType.LOGIN_TYPE_MOBILE.getTypeKey())?user.getMobile():
                            Objects.equals(loginType.getTypeKey(),CommonEnumConstant.LoginType.LOGIN_TYPE_EMAIL.getTypeKey())?user.getEmail():user.getUsername();
            redisTemplateUtils.set(RedisConstant.ACCOUNT_NON_LOCKED + lockedKey,
                    ResponseData.out(CommonEnumConstant.PromptMessage.USER_LOCK_ERROR), sysParamConfigVo.getAccountLockTime());
        } else {
            user.setErrorCount(user.getErrorCount() + 1);
        }
        baseMapper.updateById(user);
    }
}
