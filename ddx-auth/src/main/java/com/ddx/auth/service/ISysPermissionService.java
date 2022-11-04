package com.ddx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ddx.auth.entity.SysPermission;
import com.ddx.auth.model.resp.SysRolePermissionResp;

import java.util.List;

/**
 * @ClassName: ISysPermissionService
 * @Description: 权限表 服务类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<SysRolePermissionResp> listRolePermission();
}
