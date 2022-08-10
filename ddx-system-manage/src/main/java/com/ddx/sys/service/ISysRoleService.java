package com.ddx.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ddx.common.dto.resp.PaginatedResult;
import com.ddx.sys.dto.req.sysRole.SysRoleQueryReq;
import com.ddx.sys.dto.resp.sysRole.RoleKeyValResp;
import com.ddx.sys.dto.resp.sysRole.SysRoleResp;
import com.ddx.sys.entity.SysRole;

import java.util.List;

/**
 * @ClassName: ISysRoleService
 * @Description: 角色表 服务类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 分页查询
     * @param arg0
     * @param sysRoleQueryReq
     * @return
     */
    PaginatedResult selectPage(IPage<SysRole> arg0, SysRoleQueryReq sysRoleQueryReq);

    /**
     * 通过角色集合对象查找角色
     * @param roles
     * @return 返回角色以及绑定权限信息
     */
    List<SysRoleResp> selectRolePermissionByRoles(List<SysRole> roles);

    /**
     * 查询所有校色键值
     * @return
     */
    List<RoleKeyValResp> selectRoleKeyAndValAll();
}
