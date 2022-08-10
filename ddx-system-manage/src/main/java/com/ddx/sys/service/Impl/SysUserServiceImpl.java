package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.dto.resp.PaginatedResult;
import com.ddx.common.dto.vo.UserKeyValVo;
import com.ddx.common.utils.RedisTemplateUtils;
import com.ddx.sys.dto.req.sysUser.SysUserQueryReq;
import com.ddx.sys.dto.vo.resource.MenuElVo;
import com.ddx.sys.dto.resp.sysResource.TreeMenuAndElAuthResp;
import com.ddx.sys.dto.vo.resource.UserTreeMenuVo;
import com.ddx.sys.dto.resp.sysRole.SysRoleResp;
import com.ddx.sys.dto.resp.sysUser.SysUserResp;
import com.ddx.sys.entity.*;
import com.ddx.sys.mapper.SysUserMapper;
import com.ddx.sys.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName: SysUserServiceImpl
 * @Description: 用户信息表 服务实现类
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private RedisTemplateUtils redisTemplate ;
    @Autowired
    private ISysUserResourceService iSysUserResourceService;
    @Autowired
    private ISysResourceService iSysResourceService;

    public PaginatedResult selectPage(IPage<SysUser> arg0, SysUserQueryReq sysUserQueryReq){
        IPage<SysUser> userIPage = this.baseMapper.selectPage(arg0,new QueryWrapper<SysUser>().lambda()
                .eq(StringUtils.isNoneBlank(sysUserQueryReq.getEmail()),SysUser::getEmail, sysUserQueryReq.getEmail())
                .eq(sysUserQueryReq.getStatus() != null ,SysUser::getStatus, sysUserQueryReq.getStatus())
                .eq(StringUtils.isNoneBlank(sysUserQueryReq.getMobile()),SysUser::getMobile, sysUserQueryReq.getMobile())
                .like(StringUtils.isNoneBlank(sysUserQueryReq.getUsername()),SysUser::getUsername, sysUserQueryReq.getUsername())
                .like(StringUtils.isNoneBlank(sysUserQueryReq.getNickname()),SysUser::getNickname, sysUserQueryReq.getNickname())
                .orderByDesc(SysUser::getUpdateTime));
        List<SysUserResp> sysUserResps = this.selectUserInfoByUsers(userIPage.getRecords());
        return  PaginatedResult.builder()
                .resultData(sysUserResps)
                .totalPage((int) userIPage.getPages())
                .currentPage((int) userIPage.getCurrent())
                .totalCount(userIPage.getTotal())
                .build();
    }

    public List<SysUserResp> selectUserInfoByUsers(List<SysUser> sysUsers){
        List<SysUserResp> sysUserRespList = new ArrayList<>();
        sysUsers.forEach(sysUser -> {
            //查询用户角色与角色权限
            List<Long> userRoleIds = iSysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId,sysUser.getId())).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            List<SysRole> sysUserRoles = userRoleIds.size() > 0 ?iSysRoleService.list(new QueryWrapper<SysRole>().lambda().eq(SysRole::getStatus,ConstantUtils.ROLE_STATUS_0).in(SysRole::getId,userRoleIds)):null;
            List<SysRoleResp> sysRoleResps = sysUserRoles.size() > 0 ?iSysRoleService.selectRolePermissionByRoles(sysUserRoles):null;
            //查询用户资源
            List<Long> userMenuIds =  iSysUserResourceService.list(new QueryWrapper<SysUserResource>().lambda().eq(SysUserResource::getUserId, sysUser.getId())).stream().map(SysUserResource::getResourceId).collect(Collectors.toList());
            List<UserTreeMenuVo> treeMenuRespList = userMenuIds.size() > 0 ?iSysResourceService.getMenuTreeByUserAndServiceResourceIds(userMenuIds,null):null;
            List<MenuElVo> menuElRespList = userMenuIds.size() > 0 ?iSysResourceService.getMenuElByAndServiceResourceIds(userMenuIds,null,false):null;
            List<String> services = iSysResourceService.getServiceList(userMenuIds);
            sysUserRespList.add(SysUserResp.builder()
                    .id(sysUser.getId())
                    .userId(sysUser.getUserId())
                    .username(sysUser.getUsername())
                    .nickname(sysUser.getNickname())
                    .gender(sysUser.getGender())
                    .avatar(sysUser.getAvatar())
                    .mobile(sysUser.getMobile())
                    .status(sysUser.getStatus())
                    .loginService(sysUser.getLoginService())
                    .email(sysUser.getEmail())
                    .errorCount(sysUser.getErrorCount())
                    .roleList(sysRoleResps)
                    .resource(TreeMenuAndElAuthResp.builder()
                            .treeMenu(treeMenuRespList)
                            .serviceList(services)
                            .menuEl(menuElRespList).build())
                    .createTime(sysUser.getCreateTime())
                    .updateTime(sysUser.getUpdateTime())
                    .updateId(sysUser.getUpdateId())
                    .createId(sysUser.getCreateId())
                    .build());
        });
        return sysUserRespList;
    }

    @Override
    public Boolean initUserKeyVal() {
        try {
            //初始化用户键值
            List<UserKeyValVo> userKeyValVo = baseMapper.selectList(new QueryWrapper<>()).stream().map(e -> {
                return UserKeyValVo.builder().id(e.getId()).nickname(e.getNickname()).build();
            }).collect(Collectors.toList());
            redisTemplate.del(ConstantUtils.SYSTEM_USER_KEY_VAL);
            redisTemplate.lSet(ConstantUtils.SYSTEM_USER_KEY_VAL, userKeyValVo);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
