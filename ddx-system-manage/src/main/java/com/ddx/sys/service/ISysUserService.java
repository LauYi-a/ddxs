package com.ddx.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ddx.sys.entity.SysUser;
import com.ddx.sys.model.req.sysUser.SysUserAddReq;
import com.ddx.sys.model.req.sysUser.SysUserQueryReq;
import com.ddx.sys.model.resp.sysUser.SysUserResp;
import com.ddx.util.basis.model.resp.PaginatedResult;
import com.ddx.util.basis.response.BaseResponse;

import java.util.List;

/**
 * @ClassName: ISysUserService
 * @Description: 用户信息表 服务类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 分页查询
     * @param arg0
     * @param sysUserQueryReq
     * @return
     */
    PaginatedResult selectPage(IPage<SysUser> arg0, SysUserQueryReq sysUserQueryReq);

    /**
     * 查询用户详细信息集合
     * @param sysUsers
     * @return 用户信息 角色信息 权限信息 资源信息
     */
    List<SysUserResp> selectUserInfoByUsers(List<SysUser> sysUsers);

    /**
     * 添加用户信息
     * @param sysUserAddReq
     * @return
     */
    BaseResponse addUserInfo(SysUserAddReq sysUserAddReq);
}
