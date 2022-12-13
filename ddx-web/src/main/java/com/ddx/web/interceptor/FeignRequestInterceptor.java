package com.ddx.web.interceptor;

import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.model.vo.SysParamConfigVo;
import com.ddx.util.basis.utils.RequestContextUtils;
import com.ddx.util.basis.utils.StringUtil;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import com.ddx.web.config.UserOauthInfo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: FeignRequestInterceptor
 * @Description: 用于实现令牌信息中继
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Autowired
    private RedisTemplateUtil redisTemplateUtils;

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest httpServletRequest = RequestContextUtils.getRequest();
        Map<String, String> headers = getHeaders(httpServletRequest);
        SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(RedisConstant.SYS_PARAM_CONFIG);
        String gatewayTokenId = StringUtil.getUUID();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (Objects.equals(entry.getKey(), BasisConstant.GATEWAY_REQUEST)){
                template.header(entry.getKey(), gatewayTokenId);
            }else{
                template.header(entry.getKey(), entry.getValue());
            }
        }
        redisTemplateUtils.set(RedisConstant.SYSTEM_REQUEST_TOKEN+gatewayTokenId,UserOauthInfo.getCurrentUser().getRequestToken(),sysParamConfigVo.getGatewayTokenExpireTime());
    }


    /**
     * 获取原请求头
     * @param request
     * @return
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                map.put(key, value);
            }
        }
        return map;
    }
}