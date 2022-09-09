package com.ddx.gateway.config;

import cn.hutool.json.JSONUtil;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @ClassName: GatewayRequestRateLimiterGatewayFilterFactory
 * @Description: 服务限流过滤器达到限流标准后返回错误提示
 * 后期可在此处做 黑名单等操作
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
@Slf4j
@Component
public class GatewayRequestRateLimiterGatewayFilterFactory extends RequestRateLimiterGatewayFilterFactory {

  private final RateLimiter defaultRateLimiter;

  private final KeyResolver defaultKeyResolver;

  public GatewayRequestRateLimiterGatewayFilterFactory(RateLimiter defaultRateLimiter, KeyResolver defaultKeyResolver) {
    super(defaultRateLimiter, defaultKeyResolver);
    this.defaultRateLimiter = defaultRateLimiter;
    this.defaultKeyResolver = defaultKeyResolver;
  }

  @Override
  public GatewayFilter apply(Config config) {
    KeyResolver resolver = getOrDefault(config.getKeyResolver(), defaultKeyResolver);
    RateLimiter<Object> limiter = getOrDefault(config.getRateLimiter(), defaultRateLimiter);
    return (exchange, chain) -> resolver.resolve(exchange).flatMap(key -> {
      Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
      String finalRouteId = route.getId();
      return limiter.isAllowed(route.getId(), key).flatMap(response -> {
        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
          exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
        }
        if (response.isAllowed()) {
          return chain.filter(exchange);
        }
        log.warn(String.format("IP:%s调用%s服务已限流,访问接口:%s",key,finalRouteId,exchange.getRequest().getURI()));
        ServerHttpResponse httpResponse = exchange.getResponse();
        //修改code为500
        httpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        if (!httpResponse.getHeaders().containsKey("Content-Type")) {
          httpResponse.getHeaders().add("Content-Type", "application/json");
        }
        //此处无法触发全局异常处理，手动返回
        String body= JSONUtil.toJsonStr(ResponseData.out(CommonEnumConstant.PromptMessage.INTERNAL_SERVER_ERROR));
        DataBuffer buffer = httpResponse.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return httpResponse.writeWith(Mono.just(buffer));
      });
    });
  }

  private <T> T getOrDefault(T configValue, T defaultValue) {
    return (configValue != null) ? configValue : defaultValue;
  }
}