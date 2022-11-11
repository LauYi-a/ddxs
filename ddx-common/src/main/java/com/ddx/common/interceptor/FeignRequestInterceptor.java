package com.ddx.common.interceptor;

import com.ddx.util.basis.utils.RequestContextUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: OAuthServerClientCredentialsTokenEndpointFilter
 * @Description: 用于实现令牌信息中继
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest httpServletRequest = RequestContextUtils.getRequest();
        //获取RequestContextHolder中的信息
        Map<String, String> headers = getHeaders(httpServletRequest);
        //放入feign的RequestTemplate中
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            template.header(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取原请求头
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