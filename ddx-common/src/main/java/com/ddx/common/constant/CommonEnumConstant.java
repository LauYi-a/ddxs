package com.ddx.common.constant;

/**
 * @ClassName: ErrorCode
 * @Description: 统一枚举类
 * @Author: YI.LAU
 * @Date: 2022年03月18日
 * @Version: 1.0
 */
public class CommonEnumConstant {

    private CommonEnumConstant(){}

    /**
     * 提示信息枚举
     */
    public enum PromptMessage{

        /**
         * 成功类信息提示
         */
        SUCCESS(ConstantUtils.MSG_RESPONSE_CODE_INFO, ConstantUtils.MSG_TYPE_SUCCESS,"操作成功"),
        LOING_SUCCESS(ConstantUtils.MSG_RESPONSE_CODE_INFO, ConstantUtils.MSG_TYPE_SUCCESS,"登入成功"),
        REVOKE_TOKEN_YES(ConstantUtils.MSG_RESPONSE_CODE_INFO,ConstantUtils.MSG_TYPE_SUCCESS,"注销成功"),

        /**
         * 警告类信息提示
         */
        VALIDATED_FAILED(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"未查询到原数据，验证失败"),
        REDIS_NOT_RESOURCES_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"缓存中未获取到访问资源"),
        ADD_ROLE_PERMISSION_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"绑定角色权限失败"),
        DELETE_ROLE_PERMISSION_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"删除角色权限失败"),
        ADD_USER_ROLE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"绑定用户角色失败"),
        DELETE_USER_ROLE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"删除用户角色失败"),
        ADD_USER_RESOURCE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"绑定用户资源失败"),
        DELETE_USER_RESOURCE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"删除用户资源失败"),
        USER_NOT_FOUND_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户未注册"),
        QUOTA_USER_NOT_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"未找到用户"),
        USER_DISABLE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户已禁用"),
        USER_LOCK_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"账户已锁定"),
        USER_DISABLE_TIME_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"账户已锁定，请 %s 秒后重试"),
        USERNAME_OR_PASSWORD_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户名或密码错误"),
        USER_OLD_PASSWORD_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户旧密码错误"),
        USER_NEW_PASSWORD_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"新密码不一致"),
        KAFKA_SEND_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"发送消息失败: %s"),
        KAFKA_CONSUMER_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"消费消息失败: %s"),
        IN_BUSINESS_PROCESS_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"业务处理中"),
        REDIS_LOCK_KEY_ISNULL_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"分布式锁KEY为空"),
        USER_MENU_ISNULL_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户未分配可用菜单"),
        NO_TOKEN(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"无TOKEN信息"),

        /**
         * 错误类信息提示
         */
        NO_PERMISSION(ConstantUtils.MSG_RESPONSE_CODE_NO_PERMISSION,ConstantUtils.MSG_TYPE_ERROR,"无权限访问"),
        CLIENT_AUTHENTICATION_FAILED(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"客户端认证失败"),
        CLIENT_FAILED(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"客户端连接失败"),
        ID_AUTHENTICATION_FAILED(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"身份验证失败"),
        INIT_ROLE_PERMISSION_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"初始化角色权限失败"),
        INIT_USER_KEY_VAL_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"初始化用户键值失败"),
        INIT_WHITELIST_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"初始化白名单失败"),
        FAILED(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"操作失败"),
        SYS_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"服务异常,错误信息为：%s"),
        API_READ_TIMED_OUT_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"接口超时请稍后再试，超时接口：%s"),
        BUSINSEE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"业务异常"),
        KEY_STRING_INDEX_OUT_OF_BOUNDS_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"密钥字节长度越界，默认只支持 %s 位长度密钥"),
        REVOKE_TOKEN_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"注销失败"),
        UNSUPPORTED_GRANT_TYPE(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"不支持的认证模式"),
        PARAMETER_ILLEGAL_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"非法参数"),
        ILLEGAL_REQUEST_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"非法请求"),
        INTERNAL_SERVER_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"服务繁忙"),
        FREQUENT_RESPONSE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"请求繁忙"),
        INVALID_TOKEN(ConstantUtils.MSG_RESPONSE_CODE_INVALID_TOKEN,ConstantUtils.MSG_TYPE_ERROR,"TOKEN无效或过期"),

        ;

        PromptMessage(Integer code, String type,String msg){
            this.code = code;
            this.type = type;
            this.msg = msg;
        }

        private Integer code;
        private String type;
        private String msg;

        public Integer getCode() { return code; }
        public String getType() { return type; }
        public String getMsg() { return msg; }

    }

}
