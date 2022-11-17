package com.ddx.util.redis.constant;

/**
 * @ClassName: LockConstant
 * @Description: redis 统一常量池
 * @Author: YI.LAU
 * @Date: 2022年11月17日 09:51
 * @Version: 1.0
 */
public class LockConstant {

    /**
     * 获锁状态
     */
    public final static boolean STATUS_TRUE = true;
    public final static boolean STATUS_FALSE = false;

    /**
     * redis 锁 key
     */
    public final static String OAUTH_URLS="oauth2:oauth_urls"; //权限<->url对应的KEY
    public final static String JTI_KEY_PREFIX="oauth2:black:"; //JWT令牌黑名单的KEY
    public final static String ACCOUNT_NON_LOCKED ="oauth2:account_locked:";//账号锁 key
    public final static String SYS_PARAM_CONFIG="system:param:config:";//系统配置key
    public final static String SYS_PARAM_CONFIG_PRIVATE_KEY="system:param:config:private_key";//系统密钥
    public final static String WHITELIST_REQUEST="system:whitelist_request:";//访问白名单配置
    public final static String REQUEST_TIME_WHITELIST="system:request_time_whitelist:";//请求时间白名单配置
    public final static String SYSTEM_REQUEST="system:lock:request_url:";//系统请求锁
    public final static String LOG_OFFSET_LOCK="log:offset:lock_";//系统日志偏移量锁

    /**
     * redis 分布式锁
     */
    public final static String SINGLE_SERVER = "single://";    // 单机redis前缀
    public final static String CLUSTER_SERVER = "cluster://";    //集群redis前缀
    public final static String REDISSON_SINGLE_SERVER_FORMAT = "redis://%s";
    public final static int CLUSTER_SCAN_TIME = 2000;
    //redis 分布式锁配置文件选择模式
    public final static String REDIS_PATTERN_STAND_ALONE= "stand-alone"; //单机模式
    public final static String REDIS_PATTERN_MULTIPLE= "multiple"; //集群模式

    public final static String BUSINESS_OK = "业务处理成功";

}