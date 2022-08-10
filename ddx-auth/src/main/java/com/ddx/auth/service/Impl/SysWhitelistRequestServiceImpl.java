package com.ddx.auth.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.auth.entity.SysWhitelistRequest;
import com.ddx.auth.mapper.SysWhitelistRequestMapper;
import com.ddx.auth.service.ISysWhitelistRequestService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SysWhitelistRequestServiceImpl
 * @Description: 白名单路由 服务实现类
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Service
public class SysWhitelistRequestServiceImpl extends ServiceImpl<SysWhitelistRequestMapper, SysWhitelistRequest> implements ISysWhitelistRequestService {

    @Override
    public IPage<SysWhitelistRequest> selectPage(IPage<SysWhitelistRequest> arg0,  Wrapper<SysWhitelistRequest> arg1){
        return this.baseMapper.selectPage(arg0, arg1);
    }
}
