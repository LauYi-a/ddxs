package com.ddx.util.redis.constant;

/**
 * @ClassName: LockConstant
 * @Description: redis 统一常量池
 * @Author: YI.LAU
 * @Date: 2022年11月17日 09:51
 * @Version: 1.0
 */
public class RedisConstant {

    //auth 系统 redis key
    public final static String OAUTH_URLS="auth:authority:oauth_urls"; //权限<->url对应的KEY
    public final static String JTI_KEY_PREFIX="auth:logout:"; //JIT令牌黑名单的KEY
    public final static String BASIC_TOKEN="auth:basic:token:"; //basic token 模式key
    //system 系统服务 redis key
    public final static String ACCOUNT_NON_LOCKED ="system:locked:account:";//账号锁 key
    public final static String SYS_PARAM_CONFIG="system:param:config:";//系统配置key
    public final static String SYS_PARAM_CONFIG_PRIVATE_KEY="system:param:private_key";//系统密钥
    public final static String WHITELIST_RESOURCES="system:whitelist:resources:";//系统资源白名单
    public final static String WHITELIST_REQUEST="system:whitelist:request:";//访问白名单配置
    public final static String REQUEST_TIME_WHITELIST="system:whitelist:request:time:";//请求时间白名单配置
    //gateway 网关 redis key
    public final static String SYSTEM_REQUEST_TOKEN="gateway:request:token:";//网关请求令牌
    public final static String SYSTEM_REQUEST="gateway:request:locked:";//系统请求锁
    //通用 key
    public final static String VERIFICATION_CODE_MOBILE="all:verification:code:mobile:";//手机验证吗
    public final static String VERIFICATION_CODE_EMAIL="all:verification:code:email:";//邮件验证吗
    //redis 分布式锁
    public final static String SINGLE_SERVER = "single://";    // 单机redis前缀
    public final static String CLUSTER_SERVER = "cluster://";    //集群redis前缀
    public final static String REDISSON_SINGLE_SERVER_FORMAT = "redis://%s";
    public final static int CLUSTER_SCAN_TIME = 2000;
    //redis 分布式锁配置文件选择模式
    public final static String REDIS_PATTERN_STAND_ALONE= "stand-alone"; //单机模式
    public final static String REDIS_PATTERN_MULTIPLE= "multiple"; //集群模式
}
