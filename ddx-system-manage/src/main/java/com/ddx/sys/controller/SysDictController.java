package com.ddx.sys.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import com.ddx.sys.service.ISysDictService;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.response.ResponseData;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

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
}
