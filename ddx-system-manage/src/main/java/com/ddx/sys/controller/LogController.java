package com.ddx.sys.controller;

import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.es.config.EsClient;
import com.ddx.util.es.model.UserEsEneity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: LogController
 * @Description: 日志控制器
 * @Author: YI.LAU
 * @Date: 2022年04月09日
 * @Version: 1.0
 */

@Slf4j
@RestController
@RequestMapping("/log")
@Api(tags = "日志")
public class LogController {

    @Autowired
    private EsClient esClient;

    @PostMapping("/select-log")
    @ApiOperation(httpMethod = "POST",value = "查询日志")
    public BaseResponse getInfo(){
        try {
            Boolean isCreateIndex = esClient.createIndexSettingsMappings(UserEsEneity.class);
            System.out.println(isCreateIndex);
            System.out.println(esClient.aliases());
            System.out.println(esClient.indexs());
            UserEsEneity userEsEneity = new UserEsEneity();
            userEsEneity.setId(22L);
            userEsEneity.setName("xxxxa");
            userEsEneity.setAge(22);
            userEsEneity.setDec("xxxxxxxx");
            userEsEneity.setPrice(22.1);
            userEsEneity.setSku("aaa1");
            String s = esClient.addData(userEsEneity, true);
            System.out.println(s);
        }catch (Exception e){
            return ResponseData.out(CommonEnumConstant.PromptMessage.FAILED);
        }
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
