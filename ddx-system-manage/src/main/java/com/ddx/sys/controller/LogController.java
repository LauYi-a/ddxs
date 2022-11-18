package com.ddx.sys.controller;

import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

    /*@Autowired
    private IElasticsearchServiceApi elasticsearchServiceApi;*/

    @PostMapping("/select-log")
    @ApiOperation(httpMethod = "POST",value = "查询日志")
    public BaseResponse getInfo(){
       /* try {
            System.out.println(ResponseData.isSuccess(elasticsearchServiceApi.createIndexSettingsMappings(UserEsEneity.class)));
            System.out.println(elasticsearchServiceApi.aliasAll().getData());
            System.out.println(elasticsearchServiceApi.indexAll().getData());
            UserEsEneity userEsEneity = new UserEsEneity();
            userEsEneity.setId(22L);
            userEsEneity.setName("中华人民共和国");
            userEsEneity.setAge(22);
            userEsEneity.setDec("中华人民共和国万岁");
            userEsEneity.setPrice(22.1);
            userEsEneity.setSku("aaa1");
            ResponseData s = elasticsearchServiceApi.addData(userEsEneity, true);
            System.out.println(s);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseData.out(CommonEnumConstant.PromptMessage.FAILED);
        }*/
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }
}
