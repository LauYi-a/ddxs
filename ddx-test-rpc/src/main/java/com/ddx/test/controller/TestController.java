package com.ddx.test.controller;

import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.response.ResponseData;
import com.ddx.common.utils.OauthUtils;
import com.ddx.test.service.ITestApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName: TestController
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年04月09日
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class TestController implements ITestApiService {

    @PostMapping(value = "/getInfo")
    @Override
    public ResponseData getTestName() {
        log.info("访问 test rpc 接口 ...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS, OauthUtils.getCurrentUser());
    }
}
