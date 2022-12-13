package com.ddx.auth.controller;

import com.ddx.auth.service.Impl.JwtTokenUserDetailsService;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.model.req.BasicAuthorize;
import com.ddx.util.basis.model.vo.AccessTokenVo;
import com.ddx.util.basis.response.BaseResponse;
import com.ddx.util.basis.response.ResponseData;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import com.ddx.web.config.UserOauthInfo;
import com.ddx.web.entity.LoginVal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName: AuthController
 * @Description: 注销控制层
 * @Author: YI.LAU
 * @Date: 2022年04月09日  0009
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private RedisTemplateUtil redisTemplateUtils;
    @Autowired
    private JwtTokenUserDetailsService jwtTokenUserDetailsService;

    @PostMapping("/logout")
    public BaseResponse logout(){
        LoginVal loginVal = UserOauthInfo.getCurrentUser();
        log.info("令牌唯一ID：{},退出用户：{} 过期时间：{}",loginVal.getJti(),loginVal.getUsername(),loginVal.getExpireIn());
        redisTemplateUtils.set(RedisConstant.JTI_KEY_PREFIX+loginVal.getJti(),loginVal.getUsername(),loginVal.getExpireIn());
        return ResponseData.out(CommonEnumConstant.PromptMessage.REVOKE_TOKEN_YES);
    }

    @PostMapping("/basic/authorize")
    public ResponseData<AccessTokenVo> basicAuthorize(@RequestBody BasicAuthorize basicAuthorize){
        log.info("basic user authorize...");
        return ResponseData.out(CommonEnumConstant.PromptMessage.LOING_SUCCESS,jwtTokenUserDetailsService.basicAuthorizeToken(basicAuthorize));
    }
}
