package com.ddx.sys.controller;


import com.ddx.sys.service.ISysDictService;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation(httpMethod = "POST",value = "获取模块分组字典键值")
    public ResponseData<Map<String,Object>> getModulesAllGroupDictKeyValue(){
        log.info("get modules all group dict key and value start..");
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, iSysDictService.getModulesAllGroupDictKeyValue());
    }
}
