package com.ddx.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.dto.req.BatchDeleteKey;
import com.ddx.basis.dto.req.DeleteKey;
import com.ddx.basis.dto.resp.PaginatedResult;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.basis.utils.PageUtil;
import com.ddx.sys.dto.req.sysResource.SysResourceAddReq;
import com.ddx.sys.dto.req.sysResource.SysResourceEditReq;
import com.ddx.sys.dto.req.sysResource.SysResourceQueryReq;
import com.ddx.sys.dto.req.sysUser.QueryUserInfoReq;
import com.ddx.sys.dto.resp.sysResource.ServiceMenuResp;
import com.ddx.sys.entity.SysResource;
import com.ddx.sys.entity.SysUserResource;
import com.ddx.sys.service.ISysResourceService;
import com.ddx.sys.service.ISysUserResourceService;
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

/**
 * @ClassName: SysResourceController
 * @Description: 系统资源 前端控制器
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/sys-resource")
@Api(tags = "系统资源控制层")
public class SysResourceController {

    @Autowired
    private ISysResourceService iSysResourceService;
    @Autowired
    private ISysUserResourceService iSysUserResourceService;

    @PostMapping("/select-user-resource-ids")
    @ApiOperation(value = "查询用户菜单资源ID", notes = "系统资源")
    public ResponseData<List<Long>> selectUserResourceIds(@Validated @RequestBody QueryUserInfoReq queryUserInfoReq) {
        log.info("select user response ids ...");
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, iSysUserResourceService.selectUserResourceIdsByUserId(queryUserInfoReq.getId()));
    }

    @PostMapping("/select-menu-tree")
    @ApiOperation(value = "查询资源菜单树", notes = "系统资源")
    public ResponseData<List<ServiceMenuResp>> selectMenuTree() {
        log.info("select Response menu all...");
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, iSysResourceService.selectMenuTree());
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询系统资源列表", notes = "系统资源")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysResourceQueryReq sysResourceQueryReq) {
        log.info("get SysResource list..");
        int page = PageUtil.parsePage(sysResourceQueryReq.getPage(), ConstantUtils.PAGE);
        int perPage = PageUtil.parsePerPage(sysResourceQueryReq.getPerPage(), ConstantUtils.PER_PAGE);
        IPage<SysResource> dataList=iSysResourceService.selectPage(new Page<SysResource>(page, perPage),new QueryWrapper<SysResource>().lambda()
        .eq(StringUtils.isNoneBlank(sysResourceQueryReq.getResourceType()),SysResource::getResourceType, sysResourceQueryReq.getResourceType())
        .eq(StringUtils.isNoneBlank(sysResourceQueryReq.getIsExternal()),SysResource::getIsExternal, sysResourceQueryReq.getIsExternal())
        .eq(StringUtils.isNoneBlank(sysResourceQueryReq.getServiceModule()),SysResource::getServiceModule, sysResourceQueryReq.getServiceModule())
        .eq(StringUtils.isNoneBlank(sysResourceQueryReq.getComponent()),SysResource::getComponent, sysResourceQueryReq.getComponent())
        .like(StringUtils.isNoneBlank(sysResourceQueryReq.getResourceName()),SysResource::getResourceName, sysResourceQueryReq.getResourceName())
        .like(StringUtils.isNoneBlank(sysResourceQueryReq.getResourceUrl()),SysResource::getResourceName, sysResourceQueryReq.getResourceType())
        .orderByDesc(SysResource::getUpdateTime));
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, PaginatedResult.builder()
        .resultData(dataList.getRecords())
        .currentPage((int) dataList.getCurrent())
        .totalCount( dataList.getTotal())
        .totalPage((int) dataList.getPages())
        .build());
    }

    @ApiOperation(value = "添加系统资源", notes = "系统资源")
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(@Validated @RequestBody SysResourceAddReq  sysResourceAddReq) {
        log.info("add SysResource start..");
        SysResource sysResource = new SysResource();
        BeanUtils.copyProperties(sysResourceAddReq,sysResource);
        ExceptionUtils.errorBusinessException(!iSysResourceService.save(sysResource),CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "修改系统资源", notes = "系统资源")
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysResourceEditReq sysResourceEditReq) {
        log.info("edit SysResource start...");
        SysResource sysResource = iSysResourceService.getOne(new QueryWrapper<SysResource>().lambda().eq(SysResource::getId,sysResourceEditReq.getId()).last("limit 1"));
        ExceptionUtils.errorBusinessException(sysResource == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysResourceEditReq, sysResource);
        Boolean yesOrNo = iSysResourceService.update(sysResource,new QueryWrapper<SysResource>().lambda().eq(SysResource::getId,sysResource.getId()));
        ExceptionUtils.errorBusinessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "删除系统资源", notes = "系统资源")
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@Validated @RequestBody DeleteKey deleteKey) {
        log.info("delete SysResource start...");
        ExceptionUtils.errorBusinessException(!iSysResourceService.removeById(Long.valueOf(deleteKey.getKeyWord().toString())),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysUserResourceService.remove(new QueryWrapper<SysUserResource>().lambda().eq(SysUserResource::getResourceId,Long.valueOf(deleteKey.getKeyWord().toString()))), CommonEnumConstant.PromptMessage.DELETE_USER_RESOURCE_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "批量删除系统资源", notes = "系统资源")
    @PostMapping("/batch-delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchDelete(@Validated @RequestBody BatchDeleteKey batchDeleteKey) {
        log.info("batchDelete SysResource start...");
        ExceptionUtils.errorBusinessException(!iSysResourceService.removeByIds(batchDeleteKey.getKeyWords()),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysUserResourceService.remove(new QueryWrapper<SysUserResource>().lambda().in(SysUserResource::getResourceId,batchDeleteKey.getKeyWords())), CommonEnumConstant.PromptMessage.DELETE_USER_RESOURCE_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
