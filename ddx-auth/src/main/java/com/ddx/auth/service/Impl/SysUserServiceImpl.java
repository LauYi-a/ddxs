package com.ddx.auth.service.Impl;

import com.ddx.auth.entity.SysUser;
import com.ddx.auth.mapper.SysUserMapper;
import com.ddx.auth.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @ClassName: SysUserServiceImpl
 * @Description: 用户信息表 服务实现类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
