package com.ddx.sys.service;

import com.ddx.sys.entity.SysWhitelistRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @ClassName: ISysWhitelistRequestService
 * @Description: 白名单路由 服务类
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
public interface ISysWhitelistRequestService extends IService<SysWhitelistRequest> {

    /**
     * 分页查询
     * @param arg0
     * @param arg1
     * @return
     */
    IPage<SysWhitelistRequest> selectPage(IPage<SysWhitelistRequest> arg0, Wrapper<SysWhitelistRequest> arg1);

    /**
     * 初始化白名单
     * @return
     */
    Boolean initWhitelistConfig();

}
