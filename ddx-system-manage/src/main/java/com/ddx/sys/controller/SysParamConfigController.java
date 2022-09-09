package com.ddx.sys.controller;

import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.dto.vo.SysParamConfigVo;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.response.BaseResponse;
import com.ddx.basis.response.ResponseData;
import com.ddx.basis.utils.DateUtil;
import com.ddx.basis.utils.rsa.RSAUtils;
import com.ddx.common.utils.RedisTemplateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName: SysParamConfigController
 * @Description: 系统参数配置控制器
 * @Author: YI.LAU
 * @Date: 2022年04月06日  0006
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys-param-config")
@Api(tags = "系统参数配置控制器")
public class SysParamConfigController {

    @Autowired
    private RedisTemplateUtils redisTemplateUtils;

    @PostMapping("/get-sys-param-config")
    @ApiOperation(value = "系统参数查询", notes = "系统参数")
    public ResponseData getSysParamConfig(){
        log.info("get sys param config start ....");
        SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(ConstantUtils.SYS_PARAM_CONFIG);
        sysParamConfigVo.setAccessTokenTime(Long.valueOf(DateUtil.formatDuringHH(sysParamConfigVo.getAccessTokenTime(),false)));
        sysParamConfigVo.setRefreshTokenTime(Long.valueOf(DateUtil.formatMsDuring(sysParamConfigVo.getRefreshTokenTime(),false)));
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,sysParamConfigVo);
    }

    @PostMapping("/update-param-config")
    @ApiOperation(value = "系统参数修改", notes = "系统参数")
    public BaseResponse updateParamConfig(@Validated @RequestBody SysParamConfigVo sysParamConfigVo){
        log.info("update param config start ....");
        sysParamConfigVo.setAccessTokenTime(DateUtil.fromatHHMm(sysParamConfigVo.getAccessTokenTime()));
        sysParamConfigVo.setRefreshTokenTime(DateUtil.fromatDayMm(sysParamConfigVo.getRefreshTokenTime()));
        redisTemplateUtils.del(ConstantUtils.SYS_PARAM_CONFIG);
        redisTemplateUtils.set(ConstantUtils.SYS_PARAM_CONFIG,sysParamConfigVo);
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS);
    }

    @PostMapping("/get-public-key")
    @ApiOperation(value = "获取公钥", notes = "系统参数")
    public BaseResponse getPublicKey(){
        log.info("get public key start ....");
        Map<Integer,String> keyMap = RSAUtils.genKeyPair();
        redisTemplateUtils.set(ConstantUtils.SYS_PARAM_CONFIG_PRIVATE_KEY,keyMap.get(1));
        return ResponseData.out(CommonEnumConstant.PromptMessage.SUCCESS,keyMap.get(0));
    }

}
