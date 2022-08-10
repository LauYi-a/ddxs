package com.ddx.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.dto.req.BatchDeleteKey;
import com.ddx.common.dto.req.CheckKey;
import com.ddx.common.dto.req.DeleteKey;
import com.ddx.common.dto.resp.PaginatedResult;
import com.ddx.common.exception.ExceptionUtils;
import com.ddx.common.response.BaseResponse;
import com.ddx.common.response.ResponseData;
import com.ddx.common.utils.PageUtil;
import com.ddx.sys.dto.req.sysPermission.SysPermissionAddReq;
import com.ddx.sys.dto.req.sysPermission.SysPermissionEditReq;
import com.ddx.sys.dto.req.sysPermission.SysPermissionQueryReq;
import com.ddx.sys.entity.SysPermission;
import com.ddx.sys.entity.SysRolePermission;
import com.ddx.sys.service.ISysPermissionService;
import com.ddx.sys.service.ISysRolePermissionService;
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

    @PostMapping("/list")
    @ApiOperation(value = "查询权限列表", notes = "权限表")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysPermissionQueryReq sysPermissionQueryReq) {
        log.info("get SysPermission list..");
        int page = PageUtil.parsePage(sysPermissionQueryReq.getPage(), ConstantUtils.PAGE);
        int perPage = PageUtil.parsePerPage(sysPermissionQueryReq.getPerPage(), ConstantUtils.PER_PAGE);
        IPage<SysPermission> dataList=iSysPermissionService.selectPage(new Page<SysPermission>(page, perPage),new QueryWrapper<SysPermission>().lambda()
        .eq(StringUtils.isNoneBlank(sysPermissionQueryReq.getServiceModule()),SysPermission::getServiceModule, sysPermissionQueryReq.getServiceModule())
        .like(StringUtils.isNoneBlank(sysPermissionQueryReq.getName()),SysPermission::getName, sysPermissionQueryReq.getName())
        .like(StringUtils.isNoneBlank(sysPermissionQueryReq.getUrl()),SysPermission::getName, sysPermissionQueryReq.getUrl())
        .orderByDesc(SysPermission::getUpdateTime));
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, PaginatedResult.builder()
        .resultData(dataList.getRecords())
        .currentPage((int) dataList.getCurrent())
        .totalCount( dataList.getTotal())
        .totalPage((int) dataList.getPages())
        .build());
    }

    @ApiOperation(value = "添加权限", notes = "权限表")
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(@Validated @RequestBody SysPermissionAddReq  sysPermissionAddReq) {
        log.info("add SysPermission start..");
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(sysPermissionAddReq,sysPermission);
        ExceptionUtils.errorBusinessException(!iSysPermissionService.save(sysPermission),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "修改权限", notes = "权限表")
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysPermissionEditReq sysPermissionEditReq) {
        log.info("edit SysPermission start...");
        SysPermission sysPermission = iSysPermissionService.getOne(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getId,sysPermissionEditReq.getId()).last("limit 1"));
        ExceptionUtils.errorBusinessException(sysPermission == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysPermissionEditReq, sysPermission);
        Boolean yesOrNo = iSysPermissionService.update(sysPermission,new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getId,sysPermission.getId()));
        ExceptionUtils.errorBusinessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "删除权限", notes = "权限表")
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@Validated @RequestBody DeleteKey deleteKey) {
        log.info("delete SysPermission start...");
        ExceptionUtils.errorBusinessException(!iSysPermissionService.removeById(Long.valueOf(deleteKey.getKeyWord().toString())),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().lambda()
                .eq(SysRolePermission::getPermissionId,Long.valueOf(deleteKey.getKeyWord().toString()))), CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
        ExceptionUtils.errorBusinessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "批量删除权限", notes = "权限表")
    @PostMapping("/batch-delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchDelete(@Validated @RequestBody BatchDeleteKey batchDeleteKey) {
        log.info("batchDelete SysPermission start...");
        ExceptionUtils.errorBusinessException(!iSysPermissionService.removeByIds(batchDeleteKey.getKeyWords()),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().lambda()
                .in(SysRolePermission::getPermissionId,batchDeleteKey.getKeyWords())), CommonEnumConstant.PromptMessage.DELETE_ROLE_PERMISSION_ERROR);
        ExceptionUtils.errorBusinessException(!iSysPermissionService.initRolePermission(),CommonEnumConstant.PromptMessage.INIT_ROLE_PERMISSION_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "检查权限主键是否存在", notes = "权限表")
    @PostMapping("/check-key")
    public BaseResponse checkKey(@RequestBody CheckKey checkKey) {
        log.info("check SysPermission start...");
        boolean result = iSysPermissionService.count(new QueryWrapper<SysPermission>().lambda()
                .eq(SysPermission::getName,checkKey.getKeyWord()).or().eq(SysPermission::getUrl,checkKey.getKeyWord()))>0;
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,result);
    }
}
