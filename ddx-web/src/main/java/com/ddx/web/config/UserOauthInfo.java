package com.ddx.web.config;

import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.utils.RequestContextUtils;
import com.ddx.web.entity.LoginVal;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: OauthUtils
 * @Description: OAuth2.0工具类，从请求的线程中获取个人信息
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Slf4j
public class UserOauthInfo {

    /**
     * 获取当前请求登录用户的详细信息
     */
    public static LoginVal getCurrentUser(){
        LoginVal loginVal = (LoginVal) RequestContextUtils.getRequest().getAttribute(BasisConstant.LOGIN_VAL_ATTRIBUTE);
        return loginVal;
    }
}
