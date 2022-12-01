package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.sys.entity.SysPermission;
import com.ddx.sys.entity.SysRole;
import com.ddx.sys.entity.SysRolePermission;
import com.ddx.sys.mapper.SysRoleMapper;
import com.ddx.sys.model.req.sysRole.SysRoleAddReq;
import com.ddx.sys.model.req.sysRole.SysRoleQueryReq;
import com.ddx.sys.model.resp.sysRole.RoleKeyValResp;
import com.ddx.sys.model.resp.sysRole.SysRoleResp;
import com.ddx.sys.service.ISysPermissionService;
import com.ddx.sys.service.ISysRolePermissionService;
import com.ddx.sys.service.ISysRoleService;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.model.resp.PaginatedResult;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.basis.utils.SerialNumber;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SysRoleServiceImpl
 * @Description: 角色表 服务实现类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysRolePermissionService iSysRolePermissionService;
    @Autowired
    private ISysPermissionService iSysPermissionService;

    @Override
    public PaginatedResult selectPage(IPage<SysRole> arg0, SysRoleQueryReq sysRoleQueryReq){

        IPage<SysRole> sysRoleIPage = this.baseMapper.selectPage(arg0,new QueryWrapper<SysRole>().lambda()
                .eq(StringUtils.isNoneBlank(sysRoleQueryReq.getCode()),SysRole::getCode, sysRoleQueryReq.getCode())
                .eq(StringUtils.isNoneBlank(sysRoleQueryReq.getStatus() ),SysRole::getStatus, sysRoleQueryReq.getStatus())
                .like(StringUtils.isNoneBlank(sysRoleQueryReq.getName()),SysRole::getName, sysRoleQueryReq.getName())
                .orderByDesc(SysRole::getUpdateTime));
        List<SysRoleResp> sysRoleResps = this.selectRolePermissionByRoles(sysRoleIPage.getRecords());
        return PaginatedResult.builder()
                .resultData(sysRoleResps)
                .totalPage((int) sysRoleIPage.getPages())
                .currentPage((int) sysRoleIPage.getCurrent())
                .totalCount(sysRoleIPage.getTotal())
                .build();
    }

    @Override
    public List<SysRoleResp> selectRolePermissionByRoles(List<SysRole> roles){
        List<SysRoleResp> sysRoleResps = new ArrayList<>();
        roles.forEach(sysRole -> {
            //查询角色的权限中间表
            List<Long> permissionList = iSysRolePermissionService.list(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId,sysRole.getId())).stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());
            List<SysPermission> sysPermissions =  permissionList.size() > 0?iSysPermissionService.listByIds(permissionList).stream().collect(Collectors.toList()):null;
            sysRoleResps.add(SysRoleResp.builder()
                    .id(sysRole.getId())
                    .name(sysRole.getName())
                    .code(sysRole.getCode())
                    .status(sysRole.getStatus())
                    .rolePermission(CollectionUtils.isNotEmpty(sysPermissions)? sysPermissions:null)
                    .createTime(sysRole.getCreateTime())
                    .updateTime(sysRole.getUpdateTime())
                    .build());
        });
        return sysRoleResps;
    }

    @Override
    public List<RoleKeyValResp> selectRoleKeyAndValAll() {
        List<SysRole> roles = this.baseMapper.selectList(new QueryWrapper<SysRole>().lambda().eq(SysRole::getStatus, CommonEnumConstant.Dict.ROLE_STATUS_0.getDictKey()));
        List<RoleKeyValResp> roleKeyValResps = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(roles)){
            roles.forEach(item ->{
                roleKeyValResps.add(RoleKeyValResp.builder().name(item.getName()).id(item.getId()).build());
            });
        }
        return roleKeyValResps;
    }

    @Override
    public BaseResponse addRoleInfo(SysRoleAddReq sysRoleAddReq) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleAddReq,sysRole);
        sysRole.setCode(SerialNumber.newInstance(BasisConstant.ROLE_CODE, BasisConstant.DATE_FORMAT_12).toString());
        sysRole.setStatus(CommonEnumConstant.Dict.ROLE_STATUS_0.getDictKey());
        ExceptionUtils.businessException(!SqlHelper.retBool(baseMapper.insert(sysRole)),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.businessException(!iSysRolePermissionService.addRolePermissionId(sysRoleAddReq.getRolePermissionId(),sysRole.getId()),CommonEnumConstant.PromptMessage.ADD_ROLE_PERMISSION_ERROR);
        ExceptionUtils.businessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
