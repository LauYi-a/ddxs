package com.ddx.auth.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.entity.SysUser;
import com.ddx.auth.service.ISysUserService;
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

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
		String username = authenticationFailureBadCredentialsEvent.getAuthentication().getPrincipal().toString();
		SysUser user = iSysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername,username).or().eq(SysUser::getMobile,username).or().eq(SysUser::getEmail,username).last("limit 1"));
		if(!Objects.isNull(user)) {
			iSysUserService.updateErrorCount(user);
		}
	}
}
