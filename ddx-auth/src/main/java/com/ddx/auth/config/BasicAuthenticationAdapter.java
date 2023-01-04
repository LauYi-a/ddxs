package com.ddx.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.entity.SysUser;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

/**
 * @ClassName: BasicAuthentication
 * @Description: Basic 适配实现
 * @Author: YI.LAU
 * @Date: 2023年01月04日 14:27
 * @Version: 1.0
 */
@Component
public class BasicAuthenticationAdapter {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ISysUserService iSysUserService;

    /**
     * basic模式 账号密码验证
     * @param basicAuthentication
     * @return
     */
    public <T> ResponseData<T> basicAccountAuthentication(BasicAuthentication basicAuthentication){
        try {
            ExceptionUtils.businessException(Objects.isNull(basicAuthentication.getUsername()), CommonEnumConstant.PromptMessage.NO_USERNAME);
            ExceptionUtils.businessException(Objects.isNull(basicAuthentication.getPassword()),CommonEnumConstant.PromptMessage.NO_PASSWORD);
            ExceptionUtils.businessException(redisTemplateUtil.hasKey(RedisConstant.ACCOUNT_NON_LOCKED+basicAuthentication.getUsername()),
                    CommonEnumConstant.PromptMessage.USER_DISABLE_TIME_ERROR, redisTemplateUtil.getExpire(RedisConstant.ACCOUNT_NON_LOCKED+basicAuthentication.getUsername()));
            SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername,basicAuthentication.getUsername()).last("limit 1"));
            ExceptionUtils.businessException(Objects.isNull(user),CommonEnumConstant.PromptMessage.USER_NOT_FOUND_ERROR);
            ExceptionUtils.businessException(!Objects.equals(user.getAuthorizationType(), BasisConstant.AUTHORIZATION_TYPE_BASIC), CommonEnumConstant.PromptMessage.USER_AUTHORIZATION_ERROR);
            ExceptionUtils.businessException(Objects.equals(user.getStatus(),CommonEnumConstant.Dict.USER_STATUS_0.getDictKey()),CommonEnumConstant.PromptMessage.USER_DISABLE_ERROR);
            if (!passwordEncoder.matches(basicAuthentication.getPassword(), user.getPassword())){
                iSysUserService.updateErrorCount(user);
                return (ResponseData<T>) ResponseData.out(CommonEnumConstant.PromptMessage.LOGIN_PASSWORD_ERROR);
            }
            if (user.getStatus().equals(CommonEnumConstant.Dict.USER_STATUS_2.getDictKey())) {
                user.setErrorCount(0);
                user.setStatus(CommonEnumConstant.Dict.USER_STATUS_1.getDictKey());
                sysUserService.updateById(user);
            }
            return (ResponseData<T>) ResponseData.out(CommonEnumConstant.PromptMessage.LOGIN_SUCCESS,genToken(user));
        }catch (BusinessException be){
            return  (ResponseData<T>) ResponseData.out(be.getCode(),be.getType(),be.getMsg());
        }
    }

    /**
     * basic 模式手机号验证
     * @param basicAuthentication
     * @return
     */
    public AccessTokenVo basicMobileAuthentication(BasicAuthentication basicAuthentication){
        ExceptionUtils.businessException(Objects.isNull(basicAuthentication.getMobile()),CommonEnumConstant.PromptMessage.NO_MOBILE);
        ExceptionUtils.businessException(Objects.isNull(basicAuthentication.getVerificationCode()),CommonEnumConstant.PromptMessage.NO_VERIFICATION_CODE);
        //验证逻辑...
        return genToken(new SysUser());
    }

    /**
     * basic 模式邮箱验证
     * @param basicAuthentication
     * @return
     */
    public AccessTokenVo basicEmailAuthentication(BasicAuthentication basicAuthentication){
        ExceptionUtils.businessException(Objects.isNull(basicAuthentication.getEmail()),CommonEnumConstant.PromptMessage.NO_EMAIL);
        ExceptionUtils.businessException(Objects.isNull(basicAuthentication.getVerificationCode()),CommonEnumConstant.PromptMessage.NO_VERIFICATION_CODE);
        //验证逻辑...
        return genToken(new SysUser());
    }

    /**
     * 生成token信息
     * @param user
     */
    public AccessTokenVo genToken(SysUser user){
        SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtil.get(RedisConstant.SYS_PARAM_CONFIG);
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
        redisTemplateUtil.set(RedisConstant.BASIC_TOKEN+jwt,userInfoEncrypt,sysParamConfigVo.getAccessTokenTime());
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
