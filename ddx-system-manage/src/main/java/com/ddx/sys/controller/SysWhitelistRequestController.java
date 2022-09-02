package com.ddx.sys.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.common.utils.PageUtil;
import com.ddx.sys.dto.req.sysWhitelistRequest.SysWhitelistRequestAddReq;
import com.ddx.sys.dto.req.sysWhitelistRequest.SysWhitelistRequestEditReq;
import com.ddx.sys.dto.req.sysWhitelistRequest.SysWhitelistRequestQueryReq;
import com.ddx.sys.entity.SysWhitelistRequest;
import com.ddx.sys.service.ISysWhitelistRequestService;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.dto.req.BatchDeleteKey;
import com.ddx.common.dto.req.DeleteKey;
import com.ddx.common.dto.resp.PaginatedResult;
import com.ddx.common.exception.ExceptionUtils;
import com.ddx.common.response.BaseResponse;
import com.ddx.common.response.ResponseData;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @ClassName: SysWhitelistRequestController
 * @Description: 白名单路由 前端控制器
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/sys-whitelist-request")
@Api(tags = "白名单路由控制层")
public class SysWhitelistRequestController {

    @Autowired
    private ISysWhitelistRequestService iSysWhitelistRequestService;

    @PostMapping("/list")
    @ApiOperation(value = "查询白名单路由列表", notes = "白名单路由")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysWhitelistRequestQueryReq sysWhitelistRequestQueryReq) {
        log.info("get SysWhitelistRequest list..");
        int page = PageUtil.parsePage(sysWhitelistRequestQueryReq.getPage(), ConstantUtils.PAGE);
        int perPage = PageUtil.parsePerPage(sysWhitelistRequestQueryReq.getPerPage(), ConstantUtils.PER_PAGE);
        IPage<SysWhitelistRequest> dataList=iSysWhitelistRequestService.selectPage(new Page<SysWhitelistRequest>(page, perPage),new QueryWrapper<SysWhitelistRequest>().lambda()
        .eq(StringUtils.isNoneBlank(sysWhitelistRequestQueryReq.getServiceModule()),SysWhitelistRequest::getServiceModule, sysWhitelistRequestQueryReq.getServiceModule())
        .like(StringUtils.isNoneBlank(sysWhitelistRequestQueryReq.getName()),SysWhitelistRequest::getName, sysWhitelistRequestQueryReq.getName())
        .like(StringUtils.isNoneBlank(sysWhitelistRequestQueryReq.getUrl()),SysWhitelistRequest::getUrl, sysWhitelistRequestQueryReq.getUrl())
        .orderByDesc(SysWhitelistRequest::getUpdateTime));
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, PaginatedResult.builder()
        .resultData(dataList.getRecords())
        .currentPage((int) dataList.getCurrent())
        .totalCount( dataList.getTotal())
        .totalPage((int) dataList.getPages())
        .build());
    }

    @ApiOperation(value = "添加白名单路由", notes = "白名单路由")
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(@Validated @RequestBody SysWhitelistRequestAddReq  sysWhitelistRequestAddReq) {
        log.info("add SysWhitelistRequest start..");
        SysWhitelistRequest sysWhitelistRequest = new SysWhitelistRequest();
        BeanUtils.copyProperties(sysWhitelistRequestAddReq,sysWhitelistRequest);
        ExceptionUtils.errorBusinessException(!iSysWhitelistRequestService.save(sysWhitelistRequest),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysWhitelistRequestService.initWhitelistConfig(),CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "修改白名单路由", notes = "白名单路由")
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysWhitelistRequestEditReq sysWhitelistRequestEditReq) {
        log.info("edit SysWhitelistRequest start...");
        SysWhitelistRequest sysWhitelistRequest = iSysWhitelistRequestService.getOne(new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getId,sysWhitelistRequestEditReq.getId()).last("limit 1"));
        ExceptionUtils.errorBusinessException(sysWhitelistRequest == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysWhitelistRequestEditReq, sysWhitelistRequest);
        Boolean yesOrNo = iSysWhitelistRequestService.update(sysWhitelistRequest,new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getId,sysWhitelistRequest.getId()));
        ExceptionUtils.errorBusinessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysWhitelistRequestService.initWhitelistConfig(),CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "删除白名单路由", notes = "白名单路由")
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@Validated @RequestBody DeleteKey deleteKey) {
        log.info("delete SysWhitelistRequest start...");
        ExceptionUtils.errorBusinessException(!iSysWhitelistRequestService.removeById(Long.valueOf(deleteKey.getKeyWord().toString())),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysWhitelistRequestService.initWhitelistConfig(),CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "批量删除白名单路由", notes = "白名单路由")
    @PostMapping("/batch-delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchDelete(@Validated @RequestBody BatchDeleteKey batchDeleteKey) {
        log.info("batchDelete SysWhitelistRequest start...");
        ExceptionUtils.errorBusinessException(!iSysWhitelistRequestService.removeByIds(batchDeleteKey.getKeyWords()),CommonEnumConstant.PromptMessage.FAILED);
        ExceptionUtils.errorBusinessException(!iSysWhitelistRequestService.initWhitelistConfig(),CommonEnumConstant.PromptMessage.INIT_WHITELIST_ERROR);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
