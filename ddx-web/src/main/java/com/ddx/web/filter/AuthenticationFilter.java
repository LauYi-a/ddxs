package com.ddx.web.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.exception.BusinessException;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.basis.utils.ResponseUtils;
import com.ddx.util.basis.utils.sm4.SM4Utils;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import com.ddx.web.entity.LoginVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @ClassName: AuthenticationFilter
 * @Description: 微服务过滤器，解密网关传递的用户信息
 * 将其放入request中，便于后期业务方法直接获取用户信息
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplateUtil redisTemplateUtils;

    /**
     * 1.校验网关发放的令牌
     * 2.解密网关传递的信息
     * 3.将解密之后的信息封装放入到request中
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws IOException{
        //校验是否通过网关转发请求
        String gatewayRequest = request.getHeader(BasisConstant.GATEWAY_REQUEST);
        if (StrUtil.isBlank(gatewayRequest)){
            ResponseUtils.resultError(response,ResponseData.out(CommonEnumConstant.PromptMessage.ILLEGAL_REQUEST_ERROR));
        }
        if (!redisTemplateUtils.hasKey(RedisConstant.SYSTEM_REQUEST_TOKEN+gatewayRequest)){
            ResponseUtils.resultError(response,ResponseData.out(CommonEnumConstant.PromptMessage.GATEWAY_REQUEST_EXCEED_TOKEN));
        }
        //校验是否白名单请求
        String requestToken = redisTemplateUtils.get(RedisConstant.SYSTEM_REQUEST_TOKEN+gatewayRequest).toString();
        if (Boolean.valueOf(SM4Utils.decryptBase64(requestToken))){
            JSONObject jsonObject = null;
            String token = request.getHeader(BasisConstant.TOKEN_NAME);
            if (StrUtil.isNotBlank(token)){
                String json =  SM4Utils.decryptBase64(token);
                jsonObject = JSON.parseObject(json);
            }
            request.setAttribute(BasisConstant.LOGIN_VAL_ATTRIBUTE,setTokenInfo(jsonObject,requestToken));
            doFilter(request,response,filterChain);
        }else{
            //获取请求头中的加密的用户信息，只接受网关过来带了token的请求
            String token = request.getHeader(BasisConstant.TOKEN_NAME);
            if (StrUtil.isNotBlank(token)){
                try {
                    String json =  SM4Utils.decryptBase64(token);
                    JSONObject jsonObject = JSON.parseObject(json);
                    //放入request的attribute中
                    request.setAttribute(BasisConstant.LOGIN_VAL_ATTRIBUTE,setTokenInfo(jsonObject,requestToken));
                    doFilter(request,response,filterChain);
                }catch (Exception e){
                    ResponseUtils.resultError(response, ResponseData.out(CommonEnumConstant.PromptMessage.INVALID_TOKEN));
                }
            }else{
                ResponseUtils.resultError(response, ResponseData.out(CommonEnumConstant.PromptMessage.NO_TOKEN));
            }
        }
    }

    /**
     * 设置用户身份权限信息
     * @param jsonObject
     * @return
     */
    private LoginVal setTokenInfo(JSONObject jsonObject,String requestToken){
        LoginVal loginVal = new LoginVal();
        if (Objects.nonNull(jsonObject)) {
            JSONArray tempJsonArray = jsonObject.getJSONArray(BasisConstant.AUTHORITIES_NAME);
            String[] authorities = tempJsonArray.toArray(new String[0]);
            loginVal.setUserId(Long.valueOf(jsonObject.getString(BasisConstant.USER_ID)));
            loginVal.setUsername(jsonObject.getString(BasisConstant.PRINCIPAL_NAME));
            loginVal.setNickname(jsonObject.getString(BasisConstant.NICKNAME));
            loginVal.setAuthorities(authorities);
            loginVal.setJti(jsonObject.getString(BasisConstant.JTI));
            loginVal.setRequestToken(requestToken);
            loginVal.setExpireIn(jsonObject.getLong(BasisConstant.EXPR));
        }else{
            loginVal.setRequestToken(requestToken);
            loginVal.setUserId(Long.valueOf(0));
            loginVal.setUsername("-");
            loginVal.setNickname("-");
        }
        return loginVal;
    }

    /**
     * 执行下一步
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     */
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException{
        try {
            filterChain.doFilter(request,response);
        }catch (Exception e){
            try {
                BusinessException be = (BusinessException) e.getCause();
                ResponseUtils.resultError(response,ResponseData.out(be.getCode(),be.getType(),be.getMessage()));
            }catch (Exception e1){
                ResponseUtils.resultError(response,ResponseData.out(CommonEnumConstant.PromptMessage.SYS_ERROR,e.getMessage()));
            }
        }
    }
}