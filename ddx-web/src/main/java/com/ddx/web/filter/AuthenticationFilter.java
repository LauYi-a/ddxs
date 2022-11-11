package com.ddx.web.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ddx.util.basis.constant.ConstantUtils;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.exception.BusinessException;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.basis.utils.ConversionUtils;
import com.ddx.util.basis.utils.ResponseUtils;
import com.ddx.util.basis.utils.StringUtil;
import com.ddx.util.basis.utils.sm4.SM4Utils;
import com.ddx.util.redis.template.RedisTemplateUtil;
import com.ddx.web.entity.LoginVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName: AuthenticationFilter
 * @Description: 微服务过滤器，解密网关传递的用户信息，将其放入request中，便于后期业务方法直接获取用户信息
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplateUtil redisTemplateUtils;
    /**
     * 具体方法主要分为两步
     * 1. 解密网关传递的信息
     * 2. 将解密之后的信息封装放入到request中
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //白名单放行 防止直接用服务端口访问服务接口
        List<String> ignoreUrls =  ConversionUtils.castList(JSONObject.parseArray(redisTemplateUtils.get(ConstantUtils.WHITELIST_REQUEST).toString()),String.class);
        Boolean isDoFilter = false;
        if (StringUtil.checkUrls(ignoreUrls, request.getRequestURI())){
            filterChain.doFilter(request,response);
            isDoFilter = true;
        }
        //获取请求头中的加密的用户信息，只接受网关过带了token的请求
        String token = request.getHeader(ConstantUtils.TOKEN_NAME);
        if (StrUtil.isNotBlank(token)){
            String json =  SM4Utils.decryptBase64(token);
            JSONObject jsonObject = JSON.parseObject(json);
            //获取用户身份信息、权限信息
            String principal = jsonObject.getString(ConstantUtils.PRINCIPAL_NAME);
            String nickName = jsonObject.getString(ConstantUtils.NICKNAME);
            String userId=jsonObject.getString(ConstantUtils.USER_ID);
            String jti = jsonObject.getString(ConstantUtils.JTI);
            Long expireIn = jsonObject.getLong(ConstantUtils.EXPR);
            JSONArray tempJsonArray = jsonObject.getJSONArray(ConstantUtils.AUTHORITIES_NAME);
            //权限
            String[] authorities =  tempJsonArray.toArray(new String[0]);
            //放入LoginVal
            LoginVal loginVal = new LoginVal();
            loginVal.setUserId(userId);
            loginVal.setUsername(principal);
            loginVal.setNickname(nickName);
            loginVal.setAuthorities(authorities);
            loginVal.setJti(jti);
            loginVal.setExpireIn(expireIn);
            //放入request的attribute中
            request.setAttribute(ConstantUtils.LOGIN_VAL_ATTRIBUTE,loginVal);
            try {
                filterChain.doFilter(request,response);
            }catch (Exception e){
                try {
                    BusinessException be = (BusinessException) e.getCause();
                    ResponseUtils.result(response,ResponseData.out(be.getCode(),be.getType(),be.getMessage()));
                }catch (Exception e1){
                    ResponseUtils.result(response,ResponseData.out(CommonEnumConstant.PromptMessage.SYS_ERROR,e.getMessage()));
                }
            }
        }else if (!isDoFilter){
            ResponseUtils.result(response,ResponseData.out(CommonEnumConstant.PromptMessage.ILLEGAL_REQUEST_ERROR));
        }
    }
}