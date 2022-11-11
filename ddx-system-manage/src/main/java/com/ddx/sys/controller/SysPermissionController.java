package com.ddx.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.sys.entity.SysPermission;
import com.ddx.sys.entity.SysRolePermission;
import com.ddx.sys.model.req.sysPermission.PermissionQueryNoPageReq;
import com.ddx.sys.model.req.sysPermission.SysPermissionEditReq;
import com.ddx.sys.model.req.sysPermission.SysPermissionQueryReq;
import com.ddx.sys.model.resp.sysPermission.PermissionResp;
import com.ddx.sys.service.ISysPermissionService;
import com.ddx.sys.service.ISysRolePermissionService;
import com.ddx.util.basis.constant.ConstantUtils;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.model.resp.PaginatedResult;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.basis.utils.ConversionUtils;
import com.ddx.util.basis.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;


/**
 * @ClassName: SysPermissionController
 * @Description: 权限表 前端控制器
 * @author YI.LAU
 * @since 2022-04-01
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/sys-permission")
@Api(tags = "权限控制层")
public class SysPermissionController {

    @Autowired
    private ISysPermissionService iSysPermissionService;
    @Autowired
    private ISysRolePermissionService iSysRolePermissionService;

    @PostMapping("/select-permission-all")
    @ApiOperation(httpMethod = "POST",value = "查询所有权限")
    public ResponseData<List<PermissionResp>> selectPermissionAll(@Validated @RequestBody PermissionQueryNoPageReq permissionQueryNoPageReq) {
        log.info("select permission all...");
        List<SysPermission>  sysPermissions = iSysPermissionService.list(new QueryWrapper<SysPermission>().lambda().eq(StringUtils.isNoneBlank(permissionQueryNoPageReq.getIsRole()),SysPermission::getIsRole,permissionQueryNoPageReq.getIsRole()));
        List<PermissionResp> permissionResps = new ConversionUtils(PermissionResp.class).toConversionListType(sysPermissions);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,permissionResps);
    }

    @PostMapping("/list")
    @ApiOperation(httpMethod = "POST",value = "查询权限列表")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysPermissionQueryReq sysPermissionQueryReq) {
        log.info("get SysPermission list..");
        int page = PageUtil.parsePage(sysPermissionQueryReq.getPage(), ConstantUtils.PAGE);
        int perPage = PageUtil.parsePerPage(sysPermissionQueryReq.getPerPage(), ConstantUtils.PER_PAGE);
        IPage<SysPermission> dataList=iSysPermissionService.selectPage(new Page<SysPermission>(page, perPage),new QueryWrapper<SysPermission>().lambda()
        .eq(StringUtils.isNoneBlank(sysPermissionQueryReq.getServiceModule()),SysPermission::getServiceModule, sysPermissionQueryReq.getServiceModule())
        .eq(StringUtils.isNoneBlank(sysPermissionQueryReq.getIsRole()),SysPermission::getIsRole, sysPermissionQueryReq.getIsRole())
        .like(StringUtils.isNoneBlank(sysPermissionQueryReq.getName()),SysPermission::getName, sysPermissionQueryReq.getName())
        .like(StringUtils.isNoneBlank(sysPermissionQueryReq.getUrl()),SysPermission::getUrl, sysPermissionQueryReq.getUrl())
        .orderByDesc(SysPermission::getUpdateTime));
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, PaginatedResult.builder()
        .resultData(dataList.getRecords())
        .currentPage((int) dataList.getCurrent())
        .totalCount( dataList.getTotal())
        .totalPage((int) dataList.getPages())
        .build());
    }

    @PostMapping("/edit")
    @ApiOperation(httpMethod = "POST",value = "修改权限")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysPermissionEditReq sysPermissionEditReq) {
        log.info("edit SysPermission start...");
        SysPermission sysPermission = iSysPermissionService.getOne(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getId,sysPermissionEditReq.getId()).last("limit 1"));
        ExceptionUtils.businessException(sysPermission == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysPermissionEditReq, sysPermission);
        Boolean yesOrNo = iSysPermissionService.update(sysPermission,new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getId,sysPermission.getId()));
        ExceptionUtils.businessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        if (Objects.equals(sysPermission.getIsRole(),CommonEnumConstant.Dict.IS_ROLE_PERMISSION_1)){
            ExceptionUtils.businessException(!iSysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getPermissionId,sysPermission.getId())),CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
            ExceptionUtils.businessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        }
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/refresh-cache")
    @ApiOperation(httpMethod = "POST",value = "刷新权限缓存")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse refreshCache(){
        ExceptionUtils.businessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
