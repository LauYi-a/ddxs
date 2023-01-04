package com.ddx.auth.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.config.BasicAuthenticationAdapter;
import com.ddx.auth.entity.SysUser;
import com.ddx.auth.service.BasicAuthenticationService;
import com.ddx.auth.service.ISysUserService;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.BusinessException;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.model.req.BasicAuthentication;
import com.ddx.util.basis.model.vo.AccessTokenVo;
import com.ddx.util.basis.model.vo.SysParamConfigVo;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.basis.utils.StringUtil;
import com.ddx.util.basis.utils.sm4.SM4Utils;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import com.ddx.web.config.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
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
        Object adapterClass = ApplicationContextUtil.getBean(BasicAuthenticationAdapter.class);
        try {
            Method method = BasicAuthenticationAdapter.class.getDeclaredMethod(adapter,BasicAuthentication.class);
            return (ResponseData<T>) method.invoke(adapterClass,basicAuthentication);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseData.out(CommonEnumConstant.PromptMessage.ID_AUTHENTICATION_FAILED);
        }
    }
}
