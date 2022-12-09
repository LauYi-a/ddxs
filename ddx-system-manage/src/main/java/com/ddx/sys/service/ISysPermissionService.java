package com.ddx.sys.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ddx.sys.entity.SysPermission;

/**
 * @ClassName: ISysPermissionService
 * @Description: 权限表 服务类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
public interface ISysPermissionService extends IService<SysPermission> {

    /**
     * 分页查询
     * @param arg0
     * @param arg1
     * @return
     */
    IPage<SysPermission> selectPage(IPage<SysPermission> arg0, Wrapper<SysPermission> arg1);

    /**
     * 初始化角色权限
     * @return
     */
    Boolean initRolePermission();
}
