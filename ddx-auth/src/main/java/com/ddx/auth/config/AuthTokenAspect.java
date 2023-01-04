package com.ddx.auth.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.entity.SysUser;
import com.ddx.auth.service.ISysUserService;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.model.vo.AccessTokenVo;
import com.ddx.util.basis.response.ResponseData;
import io.swagger.models.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

/**
 * @ClassName: AuthTokenAspect
 * @Description: 通过AOP切面来改变OAuth2 生成access_token返回值格式
 * 后期可在此添加登入记录
 * @Author: YI.LAU
 * @Date: 2022年05月08日
 * @Version: 1.0
 */
@Component
@Aspect
public class AuthTokenAspect {

    @Autowired
    private ISysUserService iSysUserService;

    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        Response response = new Response();
        Object proceed = pjp.proceed();
        if (proceed != null) {
            ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) proceed;
            OAuth2AccessToken body = responseEntity.getBody();
            asyncInitLoginErrorCount(body);
            return ResponseEntity.ok(ResponseData.out(CommonEnumConstant.PromptMessage.LOGIN_SUCCESS,
                    AccessTokenVo.builder()
                            .access_token(body.getValue())
                            .token_type(body.getTokenType())
                            .expires_in(body.getExpiresIn())
                            .scope(body.getScope().toString())
                            .user_id(body.getAdditionalInformation().get(BasisConstant.USER_ID).toString())
                            .nickname(body.getAdditionalInformation().get(BasisConstant.NICKNAME).toString())
                            .loginService(body.getAdditionalInformation().get(BasisConstant.LOGIN_SERVICE).toString())
                            .jti(body.getAdditionalInformation().get(BasisConstant.JTI).toString())
                    .build()));
        }
        return ResponseEntity.ok(ResponseData.out(CommonEnumConstant.PromptMessage.LOGIN_SUCCESS,response));
    }

    /**
     * 异步初始登入错误次数
     * @param body
     */
    public void asyncInitLoginErrorCount(OAuth2AccessToken body){
        Thread errorCountInit = new Thread(()->{
            try {
                SysUser user = iSysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getId,body.getAdditionalInformation().get("user_id")));
                if(user != null) {
                    if(user.getErrorCount()>0){
                        user.setErrorCount(0);
                        iSysUserService.updateById(user);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        errorCountInit.start();
    }
}