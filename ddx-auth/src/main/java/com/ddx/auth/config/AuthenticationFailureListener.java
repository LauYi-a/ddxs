package com.ddx.auth.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.entity.SysUser;
import com.ddx.auth.service.ISysUserService;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.model.vo.SysParamConfigVo;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.redis.constant.LockConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @ClassName: AuthenticationFailureListener
 * @Description: 登陆失败监听
 * 失败次数限制
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> { 

	@Autowired
	private ISysUserService iSysUserService;
	@Autowired
	private RedisTemplateUtil redisTemplateUtils;

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
		String username = authenticationFailureBadCredentialsEvent.getAuthentication().getPrincipal().toString();
		SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(LockConstant.SYS_PARAM_CONFIG);
		SysUser user = iSysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername,username).or().eq(SysUser::getMobile,username).last("limit 1"));
		if(!Objects.isNull(user)) {
			if (user.getErrorCount() >= sysParamConfigVo.getLpec()) {
				user.setStatus(CommonEnumConstant.Dict.USER_STATUS_2.getDictKey());
				redisTemplateUtils.set(LockConstant.ACCOUNT_NON_LOCKED + user.getUsername(),
						ResponseData.out(CommonEnumConstant.PromptMessage.USER_LOCK_ERROR), sysParamConfigVo.getAccountLockTime());
			} else {
				user.setErrorCount(user.getErrorCount() + 1);
			}
			iSysUserService.updateById(user);
		}
	}
}
