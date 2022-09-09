package com.ddx.test.service;

import com.ddx.basis.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ClassName: ITestServer
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年04月09日
 * @Version: 1.0
 */

@FeignClient(value = "ddx-test-rpc",path = "/ddx/test")
public interface ITestApiService {

    @PostMapping("/api/getInfo")
    ResponseData getTestName();
}
