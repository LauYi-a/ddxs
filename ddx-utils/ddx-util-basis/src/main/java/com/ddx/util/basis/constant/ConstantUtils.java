package com.ddx.util.basis.constant;

/**
 * @ClassName: ConstantUtils
 * @Description: 业务统一常量池类
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */
public class ConstantUtils {

    /**
     * 提示消息级别
     */
    public static final String MSG_TYPE_ERROR = "error";
    public static final String MSG_TYPE_INFO = "info";
    public static final String MSG_TYPE_SUCCESS = "success";
    public static final String MSG_TYPE_WARNING = "warning";

    /**
     * 提示消息响应码
     */
    public static final int MSG_RESPONSE_CODE_ERROR = 9999;
    public static final int MSG_RESPONSE_CODE_INFO = 2000;
    public static final int MSG_RESPONSE_CODE_WARNING = 2099;
    public static final int MSG_RESPONSE_CODE_INVALID_TOKEN = 4003;
    public static final int MSG_RESPONSE_CODE_NO_PERMISSION = 4001;

    /**
     * 字典分组键
     */
    public static final String USER_GENDER = "userGender";
    public static final String USER_STATUS = "userStatus";
    public static final String MENU_TYPE = "menuType";
    public static final String MENU_CACHE = "menuCache";
    public static final String MENU_HIDE_TABS = "menuHideTabs";
    public static final String MENU_HIDE_CLOSE = "menuHideClose";
    public static final String MENU_HIDE = "menuHide";
    public static final String WHITELIST_TYPE = "whitelistType";
    public static final String ROLE_STATUS = "roleStatus";
    public static final String IS_ROLE_PERMISSION = "isRolePermission";
    public static final String SERVICE_MODULES_NAME = "serviceModulesName";
    public static final String SELECT_LOG_LINES = "selectLogLines";


    /**
     * 系统默认密码
     */
    public static final String INIT_PASSWORD = "ddx@";

    /**
     * 角色前缀
     */
    public final static String ROLE_PREFIX="ROLE_";
    public final static String METHOD_SUFFIX=":";
    public final static String ROLE_ROOT_CODE="ROLE_ROOT";

    /**
     * 分页参数
     */
    public static final int PAGE = 1;
    public static final int PER_PAGE = 10;

    /**
     * 符号
     */
    public static final String COLON = ":";

    /**
     * JWT的秘钥
     */
    public static final String SIGN_KEY="ddx";
    public static final String TOKEN_NAME="jwt-token";
    public static final String PRINCIPAL_NAME="principal";
    public static final String AUTHORITIES_NAME="authorities";
    public static final String USER_ID="user_id";
    public static final String NICKNAME="nickname";
    public static final String LOGIN_SERVICE="loginService;";
    public static final String JTI="jti";
    public static final String EXPR="expr";

    /**
     * SM4 国密加密
     */
    public static final String SM4_KEY = "SM4-DDX-KEY-2022";
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    public static final String ALGORITHM_NAME = "SM4";
    public static final String ENCODING = "UTF-8";
    /**
     * RequestConstant
     */
    public final static String LOGIN_VAL_ATTRIBUTE="loginVal_attribute";

    /**
     * 流水号前缀
     */
    public final static String SERIAL_LOG="SERIAL";
    public final static String SERIAL_ID="ID";
    public final static String ROLE_CODE="R";

    /**
     * 系统请求 key
     */
    public final static String REQUEST_SERIAL_NUMBER="serialNumber";//请求流水号key
    public final static String REQUEST_TYPE_REQ="REQUEST";//请求
    public final static String REQUEST_TYPE_RESP="RESPONSE";//响应


    /**
     * 日期格式化
     */
    public final static String DATE_FORMAT_1 = "yyyy-MM-dd";
    public final static String DATE_FORMAT_2 = "HH:mm:ss";
    public final static String DATE_FORMAT_3 = "yyyy年MM月dd日";
    public final static String DATE_FORMAT_4 = "HH时mm分ss秒";
    public final static String DATE_FORMAT_5 = "yyyyMMdd";
    public final static String DATE_FORMAT_6 = "HHmmss";
    public final static String DATE_FORMAT_7 = DATE_FORMAT_5+DATE_FORMAT_6;
    public final static String DATE_FORMAT_8 = DATE_FORMAT_1+" "+DATE_FORMAT_2;

    /**
     * kafka topic
     */
    public final static String DDX_SYSTEM_MANAGE_TOPIC = "ddx-system-manage-log";
    public final static String DDX_AUTH_TOPIC = "ddx-auth-log";

    /**
     * 线程持名称
     */
    public final static String THREAD_POOL_LOG_MC= "thread_pool_log_mc";
}
