package com.ddx.sys.controller;


import com.ddx.sys.dto.req.sysDict.SysDictCheckKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddx.common.utils.PageUtil;
import com.ddx.sys.dto.req.sysDict.SysDictAddReq;
import com.ddx.sys.dto.req.sysDict.SysDictEditReq;
import com.ddx.sys.dto.req.sysDict.SysDictQueryReq;
import com.ddx.sys.entity.SysDict;
import com.ddx.sys.service.ISysDictService;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.dto.req.BatchDeleteKey;
import com.ddx.common.dto.req.CheckKey;
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

import java.util.Map;

/**
 * @ClassName: SysDictController
 * @Description:  前端控制器
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/sys-dict")
@Api(tags = "字典控制层")
public class SysDictController {

    @Autowired
    private ISysDictService iSysDictService;

    @PostMapping("/get-modules-all-group-dict-key-value")
    @ApiOperation(value = "获取模块分组字典键值", notes = "")
    public ResponseData<Map<String,Object>> getModulesAllGroupDictKeyValue(){
        log.info("get modules all group dict key and value start..");
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, iSysDictService.getModulesAllGroupDictKeyValue());
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "")
    public ResponseData<PaginatedResult> getList(@Validated @RequestBody SysDictQueryReq sysDictQueryReq) {
        log.info("get SysDict list..");
        int page = PageUtil.parsePage(sysDictQueryReq.getPage(), ConstantUtils.PAGE);
        int perPage = PageUtil.parsePerPage(sysDictQueryReq.getPerPage(), ConstantUtils.PER_PAGE);
        IPage<SysDict> dataList=iSysDictService.selectPage(new Page<SysDict>(page, perPage),new QueryWrapper<SysDict>().lambda()
        .eq(StringUtils.isNoneBlank(sysDictQueryReq.getGroupType()),SysDict::getGroupType, sysDictQueryReq.getGroupType())
        .eq(StringUtils.isNoneBlank(sysDictQueryReq.getModules()),SysDict::getModules, sysDictQueryReq.getModules())
        .eq(StringUtils.isNoneBlank(sysDictQueryReq.getValue()),SysDict::getDictValue, sysDictQueryReq.getValue())
        .like(StringUtils.isNoneBlank(sysDictQueryReq.getKey()),SysDict::getDictKey, sysDictQueryReq.getKey())
        .orderByDesc(SysDict::getUpdateTime));
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, PaginatedResult.builder()
        .resultData(dataList.getRecords())
        .currentPage((int) dataList.getCurrent())
        .totalCount( dataList.getTotal())
        .totalPage((int) dataList.getPages())
        .build());
    }

    @ApiOperation(value = "添加", notes = "")
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse add(@Validated @RequestBody SysDictAddReq  sysDictAddReq) {
        log.info("add SysDict start..");
        SysDict sysDict = new SysDict();
        BeanUtils.copyProperties(sysDictAddReq,sysDict);
        ExceptionUtils.errorBusinessException(!iSysDictService.save(sysDict),CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "修改", notes = "")
    @PostMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse edit(@Validated @RequestBody SysDictEditReq sysDictEditReq) {
        log.info("edit SysDict start...");
        SysDict sysDict = iSysDictService.getOne(new QueryWrapper<SysDict>().lambda().eq(SysDict::getId,sysDictEditReq.getId()).last("limit 1"));
        ExceptionUtils.errorBusinessException(sysDict == null,CommonEnumConstant.PromptMessage.VALIDATED_FAILED);
        BeanUtils.copyProperties(sysDictEditReq, sysDict);
        Boolean yesOrNo = iSysDictService.update(sysDict,new QueryWrapper<SysDict>().lambda().eq(SysDict::getId,sysDict.getId()));
        ExceptionUtils.errorBusinessException(!yesOrNo,CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "删除", notes = "")
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse delete(@Validated @RequestBody DeleteKey deleteKey) {
        log.info("delete SysDict start...");
        ExceptionUtils.errorBusinessException(!iSysDictService.removeById(Long.valueOf(deleteKey.getKeyWord().toString())),CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "批量删除", notes = "")
    @PostMapping("/batch-delete")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchDelete(@Validated @RequestBody BatchDeleteKey batchDeleteKey) {
        log.info("batchDelete SysDict start...");
        ExceptionUtils.errorBusinessException(!iSysDictService.removeByIds(batchDeleteKey.getKeyWords()),CommonEnumConstant.PromptMessage.FAILED);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @ApiOperation(value = "检查主键是否存在", notes = "")
    @PostMapping("/check-key")
    public BaseResponse checkKey(@RequestBody CheckKey checkKey) {
        log.info("check SysDict start...");
        SysDictCheckKey dictCheckKey = (SysDictCheckKey) checkKey.getKeyWord();
        boolean result = iSysDictService.count(new QueryWrapper<SysDict>().lambda()
                .eq(SysDict::getModules,dictCheckKey.getModules()).eq(SysDict::getGroupType,dictCheckKey.getGroupType()).eq(SysDict::getDictKey,dictCheckKey.getKey()))>0;
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,result);
    }
}
