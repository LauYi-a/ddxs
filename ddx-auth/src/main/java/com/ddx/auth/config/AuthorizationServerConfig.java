package com.ddx.auth.config;

import com.ddx.auth.exception.OAuthServerAuthenticationEntryPoint;
import com.ddx.auth.exception.OAuthServerWebResponseExceptionTranslator;
import com.ddx.auth.filter.OAuthServerClientCredentialsTokenEndpointFilter;
import com.ddx.util.basis.constant.ConstantUtils;
import com.ddx.util.basis.model.vo.SysParamConfigVo;
import com.ddx.util.redis.template.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.PostConstruct;


/**
 * @ClassName: AuthorizationServerConfig
 * @Description: 认证中心的配置
 * @EnableAuthorizationServer： 这个注解标注这是一个认证中心
 * 继承AuthorizationServerConfigurerAdapter
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 令牌存储策略
     */
    @Autowired
    private TokenStore tokenStore;

    /**
     * 客户端存储策略，这里使用内存方式，后续可以存储在数据库
     */
    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * Security的认证管理器，密码模式需要用到
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private OAuthServerAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplateUtil redisTemplateUtils;


    /**
     * 初始化系统参数
     */
    @PostConstruct
    public void initSysParam(){
        if (redisTemplateUtils.hasKey(ConstantUtils.SYS_PARAM_CONFIG)) {
            SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(ConstantUtils.SYS_PARAM_CONFIG);
            redisTemplateUtils.set(ConstantUtils.SYS_PARAM_CONFIG, sysParamConfigVo);
        } else {
            redisTemplateUtils.set(ConstantUtils.SYS_PARAM_CONFIG, SysParamConfigVo.builder()
                    .lpec(4)//登入错误次数默认四次
                    .accountLockTime(Long.valueOf(3600))//默认3600秒
                    .accessTokenTime(Long.valueOf(60 * 60 * 1))// 默认1小时
                    .refreshTokenTime(Long.valueOf(60 * 60 * 24 * 1)) //默认1天
                    .sysRequestTime(Long.valueOf(5))//系统请求时间默认5秒
                    .build());
        }
        log.info("初始化系统参数配置完成...");
    }

    /**
     * 配置客户端详情，并不是所有的客户端都能接入授权服务
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //使用JdbcClientDetailsService，从数据库中加载客户端的信息
        //clients.withClientDetails(new JdbcClientDetailsService(dataSource));
        //TODO 暂定内存模式，后续可以存储在数据库中，更加方便
        clients.inMemory()
                //客户端id
                .withClient("ddx")
                //客户端秘钥
                .secret(passwordEncoder.encode("ddx-key"))
                //资源id，唯一，比如订单服务作为一个资源,可以设置多个
                .resourceIds("ddx-auth")
                //授权模式，总共四种，1. authorization_code（授权码模式）、password（密码模式）、client_credentials（客户端模式）、implicit（简化模式）
                //refresh_token并不是授权模式，
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")
                //允许的授权范围，客户端的权限，这里的all只是一种标识，可以自定义，为了后续的资源服务进行权限控制
                .scopes("all")
                //false 则跳转到授权页面
                .autoApprove(false);
    }

    /**
     * 令牌管理服务的配置
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplateUtils.get(ConstantUtils.SYS_PARAM_CONFIG);
        DefaultTokenServices services = new DefaultTokenServices();
        //客户端端配置策略
        services.setClientDetailsService(clientDetailsService);
        //支持令牌的刷新
        services.setSupportRefreshToken(true);
        //令牌服务
        services.setTokenStore(tokenStore);
        //access_token的过期时间
        services.setAccessTokenValiditySeconds(Integer.valueOf(sysParamConfigVo.getAccessTokenTime().toString()));
        //refresh_token的过期时间
        services.setRefreshTokenValiditySeconds(Integer.valueOf(sysParamConfigVo.getRefreshTokenTime().toString()));
        //设置令牌增强，使用JwtAccessTokenConverter进行转换
        services.setTokenEnhancer(jwtAccessTokenConverter);
        return services;
    }


    /**
     * 授权码模式的service，使用授权码模式authorization_code必须注入
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        //return new JdbcAuthorizationCodeServices(dataSource);
        //todo 授权码暂时存在内存中，后续可以存储在数据库中
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 配置令牌访问的端点
     */
    @Override
    @SuppressWarnings("ALL")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //将自定义的授权类型添加到tokenGranters中
       /* List<TokenGranter> tokenGranters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        tokenGranters.add(new MobilePwdGranter(authenticationManager, tokenServices(), clientDetailsService,
                new DefaultOAuth2RequestFactory(clientDetailsService)));*/
                //设置异常WebResponseExceptionTranslator，用于处理用户名，密码错误、授权类型不正确的异常
        endpoints .exceptionTranslator(new OAuthServerWebResponseExceptionTranslator())
                //授权码模式所需要的authorizationCodeServices
                .authorizationCodeServices(authorizationCodeServices())
                //密码模式所需要的authenticationManager
                .authenticationManager(authenticationManager)
                //令牌管理服务，无论哪种模式都需要
                .tokenServices(tokenServices())
                //添加进入tokenGranter
                //.tokenGranter(new CompositeTokenGranter(tokenGranters))
                //只允许POST提交访问令牌，uri：/oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 配置令牌访问的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        //自定义ClientCredentialsTokenEndpointFilter，用于处理客户端id，密码错误的异常
        OAuthServerClientCredentialsTokenEndpointFilter endpointFilter = new OAuthServerClientCredentialsTokenEndpointFilter(security,authenticationEntryPoint);
        endpointFilter.afterPropertiesSet();
        security.addTokenEndpointAuthenticationFilter(endpointFilter);
        security.authenticationEntryPoint(authenticationEntryPoint)
                //开启/oauth/token_key验证端口权限访问
                .tokenKeyAccess("permitAll()")
                //开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("permitAll()");
                //一定不要添加allowFormAuthenticationForClients，否则自定义的 OAuthServerClientCredentialsTokenEndpointFilter不生效
                //.allowFormAuthenticationForClients();
    }
}