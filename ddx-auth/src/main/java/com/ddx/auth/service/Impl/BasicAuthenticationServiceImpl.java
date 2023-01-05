package com.ddx.auth.service.Impl;

import com.ddx.auth.config.BasicAuthenticationAdapter;
import com.ddx.auth.service.BasicAuthenticationService;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.model.req.BasicAuthentication;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.web.config.ApplicationContextUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @ClassName: BasicAuthorizeServiceImpl
 * @Description: Basic 模式认证实现
 * @Author: YI.LAU
 * @Date: 2023年01月03日 11:27
 * @Version: 1.0
 */
@Service
public class BasicAuthenticationServiceImpl implements BasicAuthenticationService {

    @Override
    public <T> ResponseData<T> basicAuthenticationGetToken(BasicAuthentication basicAuthentication) {
        String adapter = CommonEnumConstant.LoginType.getAdapterByTypeKey(basicAuthentication.getLoginType());
        ExceptionUtils.businessException(Objects.isNull(adapter),CommonEnumConstant.PromptMessage.UNSUPPORTED_GRANT_TYPE);
        try {
            Object adapterClassBean = ApplicationContextUtil.getBean(BasicAuthenticationAdapter.class);
            Method adapterMethod = BasicAuthenticationAdapter.class.getDeclaredMethod(adapter,BasicAuthentication.class);
            return (ResponseData<T>) adapterMethod.invoke(adapterClassBean,basicAuthentication);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseData.out(CommonEnumConstant.PromptMessage.ID_AUTHENTICATION_FAILED);
        }
    }
}
