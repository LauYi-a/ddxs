package com.ddx.auth.controller;

import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.entity.LoginVal;
import com.ddx.common.response.BaseResponse;
import com.ddx.common.response.ResponseData;
import com.ddx.common.utils.OauthUtils;
import com.ddx.common.utils.RedisTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    private RedisTemplateUtils redisTemplateUtils;

    @PostMapping("/logout")
    public BaseResponse logout(){
        LoginVal loginVal = OauthUtils.getCurrentUser();
        log.info("令牌唯一ID：{},退出用户：{} 过期时间：{}",loginVal.getJti(),loginVal.getUsername(),loginVal.getExpireIn());
        redisTemplateUtils.set(ConstantUtils.JTI_KEY_PREFIX+loginVal.getJti(),loginVal.getUsername(),loginVal.getExpireIn());
        return ResponseData.out(CommonEnumConstant.PromptMessage.REVOKE_TOKEN_YES);
    }
}
