package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.basis.dto.resp.PaginatedResult;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.sys.dto.req.sysUser.SysUserAddReq;
import com.ddx.sys.dto.req.sysUser.SysUserQueryReq;
import com.ddx.sys.dto.resp.sysResource.TreeMenuAndElAuthResp;
import com.ddx.sys.dto.resp.sysRole.SysRoleResp;
import com.ddx.sys.dto.resp.sysUser.SysUserResp;
import com.ddx.sys.dto.vo.resource.MenuElVo;
import com.ddx.sys.dto.vo.resource.UserTreeMenuVo;
import com.ddx.sys.dto.vo.user.UserAddVo;
import com.ddx.sys.entity.SysRole;
import com.ddx.sys.entity.SysUser;
import com.ddx.sys.entity.SysUserResource;
import com.ddx.sys.entity.SysUserRole;
import com.ddx.sys.mapper.SysUserMapper;
import com.ddx.sys.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysUserResourceService iSysUserResourceService;
    @Autowired
    private ISysResourceService iSysResourceService;

    @Override
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

    @Override
    public List<SysUserResp> selectUserInfoByUsers(List<SysUser> sysUsers){
        List<SysUserResp> sysUserRespList = new ArrayList<>();
        sysUsers.forEach(sysUser -> {
            //查询用户角色与角色权限
            List<Long> userRoleIds = iSysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId,sysUser.getId())).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            List<SysRole> sysUserRoles = CollectionUtils.isNotEmpty(userRoleIds)?iSysRoleService.list(new QueryWrapper<SysRole>().lambda().eq(SysRole::getStatus, CommonEnumConstant.Dict.ROLE_STATUS_0.getDictKey()).in(SysRole::getId,userRoleIds)):null;
            List<SysRoleResp> sysRoleResps = CollectionUtils.isNotEmpty(sysUserRoles)?iSysRoleService.selectRolePermissionByRoles(sysUserRoles):null;
            //查询用户资源
            List<Long> userMenuIds =  iSysUserResourceService.list(new QueryWrapper<SysUserResource>().lambda().eq(SysUserResource::getUserId, sysUser.getId())).stream().map(SysUserResource::getResourceId).collect(Collectors.toList());
            List<UserTreeMenuVo> treeMenuRespList = CollectionUtils.isNotEmpty(userMenuIds) ?iSysResourceService.getMenuTreeByUserAndServiceResourceIds(userMenuIds,null):null;
            List<MenuElVo> menuElRespList = CollectionUtils.isNotEmpty(userMenuIds)?iSysResourceService.getMenuElByAndServiceResourceIds(userMenuIds,null,false):null;
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
    public BaseResponse addUserInfo(SysUserAddReq sysUserAddReq) {
        try {
            UserAddVo userAddVo = new UserAddVo();
            BeanUtils.copyProperties(sysUserAddReq,userAddVo);
            List<String> services = iSysResourceService.getServiceList(sysUserAddReq.getResourceIds());
            userAddVo.setLoginService(CollectionUtils.isNotEmpty(services)?services.get(0):null);
            SysUser sysUser = UserAddVo.getSysUser(userAddVo);
            if (SqlHelper.retBool(this.baseMapper.selectCount(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername,sysUser.getUsername())))){
                return ResponseData.out(CommonEnumConstant.PromptMessage.USER_USERNAME_EXISTING_ERROR);
            }
            if (SqlHelper.retBool(this.baseMapper.selectCount(new QueryWrapper<SysUser>().lambda().eq(SysUser::getMobile,sysUser.getMobile())))){
                return ResponseData.out(CommonEnumConstant.PromptMessage.USER_MOBILE_EXISTING_ERROR);
            }
            if (!SqlHelper.retBool(this.baseMapper.insert(sysUser))){
                return ResponseData.out(CommonEnumConstant.PromptMessage.FAILED);
            }
            if (!iSysUserRoleService.addUserRoleId(sysUserAddReq.getRoleIds(),sysUser.getId())){
                return ResponseData.out(CommonEnumConstant.PromptMessage.ADD_USER_ROLE_ERROR);
            }
            if (!iSysUserResourceService.addUserResourceId(sysUserAddReq.getResourceIds(),sysUser.getId())){
                return ResponseData.out(CommonEnumConstant.PromptMessage.ADD_ROLE_PERMISSION_ERROR);
            }
        }catch (Exception e){
            log.error("添加用户失败",e);
            ExceptionUtils.businessException(CommonEnumConstant.PromptMessage.FAILED,e.getMessage());
        }
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
