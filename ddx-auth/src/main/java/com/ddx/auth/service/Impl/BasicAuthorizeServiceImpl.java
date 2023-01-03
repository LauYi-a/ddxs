package com.ddx.auth.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.entity.SysUser;
import com.ddx.auth.service.BasicAuthorizeService;
import com.ddx.auth.service.ISysUserService;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.ddx.util.basis.model.req.BasicAuthorize;
import com.ddx.util.basis.model.vo.AccessTokenVo;
import com.ddx.util.basis.model.vo.SysParamConfigVo;
import com.ddx.util.basis.utils.StringUtil;
import com.ddx.util.basis.utils.sm4.SM4Utils;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
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
public class BasicAuthorizeServiceImpl implements BasicAuthorizeService {

    @Autowired
    private RedisTemplateUtil redisTemplateUtils;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ISysUserService iSysUserService;

    @Override
    public AccessTokenVo basicAuthorizeToken(BasicAuthorize basicAuthorize)throws Exception {
        String adapter = CommonEnumConstant.LoginType.getAdapterByTypeKey(basicAuthorize.getLoginType());
        ExceptionUtils.businessException(Objects.isNull(adapter),CommonEnumConstant.PromptMessage.UNSUPPORTED_GRANT_TYPE);
        Class<BasicAuthorizeServiceImpl> clazz = BasicAuthorizeServiceImpl.class;
        Method method = clazz.getDeclaredMethod(adapter, BasicAuthorize.class);
        return (AccessTokenVo) method.invoke(clazz.newInstance(), basicAuthorize);
    }

    /**
     * basic模式 账号密码验证
     * @param basicAuthorize
     * @return
     */
    private AccessTokenVo basicAccountAuthorize(BasicAuthorize basicAuthorize){
        ExceptionUtils.businessException(Objects.isNull(basicAuthorize.getUsername()),CommonEnumConstant.PromptMessage.NO_USERNAME);
        ExceptionUtils.businessException(Objects.isNull(basicAuthorize.getPassword()),CommonEnumConstant.PromptMessage.NO_PASSWORD);
        ExceptionUtils.businessException(redisTemplateUtils.hasKey(RedisConstant.ACCOUNT_NON_LOCKED+basicAuthorize.getUsername()),
                CommonEnumConstant.PromptMessage.USER_DISABLE_TIME_ERROR, redisTemplateUtils.getExpire(RedisConstant.ACCOUNT_NON_LOCKED+basicAuthorize.getUsername()));
        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername,basicAuthorize.getUsername()).last("limit 1"));
        ExceptionUtils.businessException(Objects.isNull(user),CommonEnumConstant.PromptMessage.USER_NOT_FOUND_ERROR);
        ExceptionUtils.businessException(!Objects.equals(user.getAuthorizationType(),BasisConstant.AUTHORIZATION_TYPE_BASIC), CommonEnumConstant.PromptMessage.USER_AUTHORIZATION_ERROR);
        ExceptionUtils.businessException(Objects.equals(user.getStatus(),CommonEnumConstant.Dict.USER_STATUS_0.getDictKey()),CommonEnumConstant.PromptMessage.USER_DISABLE_ERROR);
        String encodePassword = passwordEncoder.encode(basicAuthorize.getPassword());
        if (!passwordEncoder.matches(encodePassword, user.getPassword())){
            iSysUserService.updateErrorCount(user);
        }
        if (user.getStatus().equals(CommonEnumConstant.Dict.USER_STATUS_2.getDictKey())) {
            user.setErrorCount(0);
            user.setStatus(CommonEnumConstant.Dict.USER_STATUS_1.getDictKey());
            sysUserService.updateById(user);
        }
        return genToken(user);
    }

    /**
     * basic 模式手机号验证
     * @param basicAuthorize
     * @return
     */
    private AccessTokenVo basicMobileAuthorize(BasicAuthorize basicAuthorize){
        ExceptionUtils.businessException(Objects.isNull(basicAuthorize.getMobile()),CommonEnumConstant.PromptMessage.NO_MOBILE);
        ExceptionUtils.businessException(Objects.isNull(basicAuthorize.getVerificationCode()),CommonEnumConstant.PromptMessage.NO_VERIFICATION_CODE);
        return genToken(new SysUser());
    }

    /**
     * basic 模式邮箱验证
     * @param basicAuthorize
     * @return
     */
    private AccessTokenVo basicEmailAuthorize(BasicAuthorize basicAuthorize){
        ExceptionUtils.businessException(Objects.isNull(basicAuthorize.getEmail()),CommonEnumConstant.PromptMessage.NO_EMAIL);
        ExceptionUtils.businessException(Objects.isNull(basicAuthorize.getVerificationCode()),CommonEnumConstant.PromptMessage.NO_VERIFICATION_CODE);
        return genToken(new SysUser());
    }

    /**
     * 生成token信息
     * @param user
     */
    private AccessTokenVo  genToken(SysUser user){
        SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(RedisConstant.SYS_PARAM_CONFIG);
        String jwt = StringUtil.getUUID();//jwt令牌
        String jti = StringUtil.getUUID();//jti令牌
        //用户账号信息
        JSONObject userInfo = new JSONObject();
        userInfo.put(BasisConstant.PRINCIPAL_NAME, user.getUsername());
        userInfo.put(BasisConstant.AUTHORITIES_NAME, Arrays.asList("-"));
        userInfo.put(BasisConstant.USER_ID, String.valueOf(user.getId()));
        userInfo.put(BasisConstant.NICKNAME, user.getNickname());
        //令牌配置信息
        JSONObject jwtConfig = new JSONObject();
        jwtConfig.put(BasisConstant.EXPR, sysParamConfigVo.getAccessTokenTime());
        jwtConfig.put(BasisConstant.JTI, jti);
        //加密用户账号信息令牌配置信息
        String configEncrypt = SM4Utils.encryptBase64(jwtConfig.toJSONString());
        String userInfoEncrypt = SM4Utils.encryptBase64(userInfo.toJSONString());
        //组装token信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(BasisConstant.JWT,jwt);
        jsonObject.put(BasisConstant.JWT_CONFIG,configEncrypt);
        //加密最终token
        String token = SM4Utils.encryptBase64(jsonObject.toJSONString());
        redisTemplateUtils.set(RedisConstant.BASIC_TOKEN+jwt,userInfoEncrypt,sysParamConfigVo.getAccessTokenTime());
        return AccessTokenVo.builder()
                .access_token(token)
                .token_type(BasisConstant.AUTHORIZATION_TYPE_BASIC)
                .expires_in(Integer.valueOf(String.valueOf(sysParamConfigVo.getAccessTokenTime())))
                .scope("ALL")
                .user_id(String.valueOf(user.getId()))
                .nickname(user.getNickname())
                .loginService(user.getLoginService())
                .jti(jti)
                .build();
    }
}
