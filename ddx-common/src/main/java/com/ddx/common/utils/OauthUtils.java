package com.ddx.common.utils;

import com.ddx.common.entity.LoginVal;
import com.ddx.util.basis.constant.ConstantUtils;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.utils.RequestContextUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: OauthUtils
 * @Description: OAuth2.0工具类，从请求的线程中获取个人信息
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Slf4j
public class OauthUtils {

    /**
     * 获取当前请求登录用户的详细信息
     */
    public static LoginVal getCurrentUser(){
        try {
            LoginVal loginVal = (LoginVal) RequestContextUtils.getRequest().getAttribute(ConstantUtils.LOGIN_VAL_ATTRIBUTE);
            ExceptionUtils.businessException(loginVal == null, CommonEnumConstant.PromptMessage.NO_TOKEN);
            return loginVal;
        }catch (Exception e){
            return LoginVal.builder().userId("-1").nickname("未登入").build();
        }
    }
}
