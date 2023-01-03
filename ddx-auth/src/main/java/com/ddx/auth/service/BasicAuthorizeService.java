package com.ddx.auth.service;

import com.ddx.util.basis.model.req.BasicAuthorize;
import com.ddx.util.basis.model.vo.AccessTokenVo;

/**
 * @ClassName: BasicAuthorizeService
 * @Description: Basic 模式认证接口
 * @Author: YI.LAU
 * @Date: 2023年01月03日 11:24
 * @Version: 1.0
 */
public interface BasicAuthorizeService {

    /**
     * basic 模式认证 token
     * @return
     */
    AccessTokenVo basicAuthorizeToken(BasicAuthorize basicAuthorize) throws Exception;
}
