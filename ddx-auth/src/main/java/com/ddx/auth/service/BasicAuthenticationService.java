package com.ddx.auth.service;

import com.ddx.util.basis.model.req.BasicAuthentication;
import com.ddx.util.basis.model.vo.AccessTokenVo;
import com.ddx.util.basis.response.ResponseData;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: BasicAuthorizeService
 * @Description: Basic 模式认证接口
 * @Author: YI.LAU
 * @Date: 2023年01月03日 11:24
 * @Version: 1.0
 */
public interface BasicAuthenticationService {

    /**
     * basic 模式认证 获取token
     * @return
     */
    <T> ResponseData<T> basicAuthenticationGetToken(BasicAuthentication basicAuthorize);
}
