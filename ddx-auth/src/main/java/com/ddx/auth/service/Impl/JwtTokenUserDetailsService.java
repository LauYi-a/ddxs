package com.ddx.auth.service.Impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.entity.SysRole;
import com.ddx.auth.entity.SysUser;
import com.ddx.auth.entity.SysUserRole;
import com.ddx.auth.service.ISysRoleService;
import com.ddx.auth.service.ISysUserRoleService;
import com.ddx.auth.service.ISysUserService;
import com.ddx.common.entity.SecurityUser;
import com.ddx.util.basis.constant.ConstantUtils;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.redis.template.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: JwtTokenUserDetailsService
 * @Description: UserDetailsService的实现类，从数据库加载用户的信息，比如密码、角色
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Service
public class JwtTokenUserDetailsService implements UserDetailsService {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private RedisTemplateUtil redisTemplateUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ExceptionUtils.businessException(redisTemplateUtils.hasKey(ConstantUtils.ACCOUNT_NON_LOCKED+username), CommonEnumConstant.PromptMessage.USER_DISABLE_TIME_ERROR, redisTemplateUtils.getExpire(ConstantUtils.ACCOUNT_NON_LOCKED+username));
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername,username).or().eq(SysUser::getMobile,username).last("limit 1"));
        ExceptionUtils.businessException(Objects.isNull(user),CommonEnumConstant.PromptMessage.USER_NOT_FOUND_ERROR);
        ExceptionUtils.businessException(user.getStatus().equals(CommonEnumConstant.Dict.USER_STATUS_0.getDictKey()),CommonEnumConstant.PromptMessage.USER_DISABLE_ERROR);
        if (user.getStatus().equals(CommonEnumConstant.Dict.USER_STATUS_2.getDictKey())) {
            user.setErrorCount(0);
            user.setStatus(CommonEnumConstant.Dict.USER_STATUS_1.getDictKey());
            sysUserService.updateById(user);
        }
        //角色
        List<SysUserRole> sysUserRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId,user.getId()));
        //该用户（角色）的所有权限
        List<String> roles=new ArrayList<>();
        for (SysUserRole userRole : sysUserRoles) {
           sysRoleService.list(new QueryWrapper<SysRole>().lambda().eq(SysRole::getId,userRole.getRoleId()).eq(SysRole::getStatus,CommonEnumConstant.Dict.ROLE_STATUS_0.getDictKey()))
                   .forEach(o-> roles.add(ConstantUtils.ROLE_PREFIX+o.getCode()));
        }
        ExceptionUtils.businessException(roles.size() == 0,CommonEnumConstant.PromptMessage.ID_AUTHENTICATION_FAILED );
        return SecurityUser.builder()
                .userId(Long.valueOf(user.getId()))
                .username(user.getUsername())
                .nickname(user.getNickname())
                .loginService(user.getLoginService())
                .password(user.getPassword())
                //将角色放入authorities中
                .authorities(AuthorityUtils.createAuthorityList(ArrayUtil.toArray(roles,String.class)))
                .build();
    }
}
