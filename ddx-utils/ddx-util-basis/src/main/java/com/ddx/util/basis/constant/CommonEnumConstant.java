package com.ddx.util.basis.constant;

import com.ddx.util.basis.model.vo.DictVo;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: CommonEnumConstant
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
        SUCCESS(BasisConstant.MSG_RESPONSE_CODE_INFO, BasisConstant.MSG_TYPE_SUCCESS,"操作成功"),
        LOGIN_SUCCESS(BasisConstant.MSG_RESPONSE_CODE_INFO, BasisConstant.MSG_TYPE_SUCCESS,"登入成功"),
        REVOKE_TOKEN_YES(BasisConstant.MSG_RESPONSE_CODE_INFO, BasisConstant.MSG_TYPE_SUCCESS,"注销成功"),

        /**
         * 警告类信息提示
         */
        VALIDATED_FAILED(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"未查询到原数据，验证失败"),
        REDIS_NOT_RESOURCES_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"缓存中未获取到访问资源"),
        USER_NOT_FOUND_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"用户未注册"),
        USER_USERNAME_EXISTING_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"账号已注册"),
        USER_MOBILE_EXISTING_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"手机号码已使用"),
        USER_EMAIL_EXISTING_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"邮箱已使用"),
        QUOTA_USER_NOT_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"未找到用户"),
        USER_DISABLE_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"用户已禁用"),
        USER_LOCK_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"账户已锁定"),
        USER_DISABLE_TIME_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"账户已锁定，请 %s 秒后重试"),
        USERNAME_OR_PASSWORD_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"用户名或密码错误"),
        USER_OLD_PASSWORD_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"用户旧密码错误"),
        USER_NEW_PASSWORD_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"新密码不一致"),
        USER_MENU_ISNULL_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"用户未分配可用菜单"),
        BOUND_USER_ROLE_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"角色已绑定用户不允许删除，若需要删除请将相关用户角色解绑【%s】"),
        NO_TOKEN(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"无TOKEN信息"),
        NO_USERNAME(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"用户账号不能为空"),
        NO_PASSWORD(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"用密码不能为空"),
        NO_MOBILE(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"手机号不能为空"),
        NO_EMAIL(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"邮箱号不能为空"),
        NO_VERIFICATION_CODE(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"验证码不能为空"),
        WHITELIST_ERROR(BasisConstant.MSG_RESPONSE_CODE_WARNING, BasisConstant.MSG_TYPE_WARNING,"白名单已存在"),

        /**
         * 错误类信息提示
         */
        FAILED(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"操作失败"),
        NO_PERMISSION(BasisConstant.MSG_RESPONSE_CODE_NO_PERMISSION, BasisConstant.MSG_TYPE_ERROR,"无权限访问"),
        CLIENT_AUTHENTICATION_FAILED(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"客户端认证失败"),
        CLIENT_FAILED(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"客户端连接失败"),
        ID_AUTHENTICATION_FAILED(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"身份验证失败"),
        INIT_ROLE_PERMISSION_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"初始化角色权限失败"),
        INIT_WHITELIST_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"初始化时效白名单失败"),
        SYS_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"服务异常,错误信息为：%s"),
        SYS_NULL_POINTER_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"系统空指针异常"),
        API_READ_TIMED_OUT_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"接口超时请稍后再试，超时接口：%s"),
        UNSUPPORTED_GRANT_TYPE(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"不支持的认证模式"),
        PARAMETER_ILLEGAL_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"非法参数"),
        ILLEGAL_REQUEST_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"非法请求"),
        INTERNAL_SERVER_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"服务繁忙"),
        FREQUENT_RESPONSE_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"请求繁忙"),
        INVALID_TOKEN(BasisConstant.MSG_RESPONSE_CODE_INVALID_TOKEN, BasisConstant.MSG_TYPE_ERROR,"TOKEN无效"),
        EXCEED_TOKEN(BasisConstant.MSG_RESPONSE_CODE_INVALID_TOKEN, BasisConstant.MSG_TYPE_ERROR,"TOKEN过期"),
        USER_AUTHORIZATION_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"用户认证失败"),
        GATEWAY_REQUEST_EXCEED_TOKEN(BasisConstant.MSG_RESPONSE_CODE_INVALID_TOKEN, BasisConstant.MSG_TYPE_ERROR,"网关令牌过期"),
        ADD_ROLE_PERMISSION_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"绑定角色权限失败"),
        DELETE_ROLE_PERMISSION_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"删除角色权限失败"),
        ADD_USER_ROLE_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"绑定用户角色失败"),
        DELETE_USER_ROLE_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"删除用户角色失败"),
        ADD_USER_RESOURCE_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"绑定用户资源失败"),
        DELETE_USER_RESOURCE_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"删除用户资源失败"),
        LOGIN_PASSWORD_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"登入密码错误"),
        VERIFICATION_CODE_ERROR(BasisConstant.MSG_RESPONSE_CODE_ERROR, BasisConstant.MSG_TYPE_ERROR,"验证码错误"),
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

    /**
     * 字典枚举
     * 字典名称按分组类型加排序号来命名
     * 为保证前端与后端枚举一致性将字典改为枚举类不存数据库
     * 前端调用 getDictList 方法获取全部枚举
     */
    public enum Dict {

        USER_GENDER_0("1","男", BasisConstant.USER_GENDER,0,"sys","用户性别"),
        USER_GENDER_1("2","女", BasisConstant.USER_GENDER,1,"sys","用户性别"),
        USER_STATUS_0("0","禁用", BasisConstant.USER_STATUS,0,"sys","用户禁止使用"),
        USER_STATUS_1("1","正常", BasisConstant.USER_STATUS,1,"sys","用户可正常使用"),
        USER_STATUS_2("2","已锁", BasisConstant.USER_STATUS,2,"sys","用户已被锁定"),
        AUTHORIZATION_TYPE_BASIC("basic","Basic", BasisConstant.AUTHORIZATION_TYPE,0,"sys","其他客户端使用该类型认证用户"),
        AUTHORIZATION_TYPE_OAUTH("bearer","Bearer", BasisConstant.AUTHORIZATION_TYPE,1,"sys","平台管理账号使用该认证类型进行认证"),
        MENU_TYPE_0("0","页签", BasisConstant.MENU_TYPE,0,"sys","页签类型"),
        MENU_TYPE_1("1","菜单", BasisConstant.MENU_TYPE,1,"sys","菜单类型"),
        MENU_TYPE_2("2","元素", BasisConstant.MENU_TYPE,2,"sys","元素类型"),
        MENU_CACHE_TRUE(true,"缓存", BasisConstant.MENU_CACHE,0,"sys","菜单可加入缓存"),
        MENU_CACHE_FALSE(false,"不缓存", BasisConstant.MENU_CACHE,1,"sys","菜单不可加入缓存"),
        MENU_HIDE_TABS_TRUE(false,"显示", BasisConstant.MENU_HIDE_TABS,0,"sys","显示tabs标签在标签栏"),
        MENU_HIDE_TABS_FALSE(true,"不显示", BasisConstant.MENU_HIDE_TABS,1,"sys","不显示tabs标签在标签栏"),
        MENU_HIDE_CLOSE_TRUE(false,"可关闭", BasisConstant.MENU_HIDE_CLOSE,0,"sys","可以关闭tabs标签"),
        MENU_HIDE_CLOSE_FALSE(true,"不可关闭", BasisConstant.MENU_HIDE_CLOSE,1,"sys","不可以关闭tabs标签"),
        MENU_HIDE_TRUE(true,"隐藏", BasisConstant.MENU_HIDE,0,"sys","隐藏菜单栏"),
        MENU_HIDE_FALSE(false,"不隐藏", BasisConstant.MENU_HIDE,1,"sys","不隐藏菜单栏"),
        WHITELIST_TYPE_0("0","系统资源白名单", BasisConstant.WHITELIST_TYPE,0,"sys","系统资源白名单修改白名单后需要重新启动网关加载白名单后生效"),
        WHITELIST_TYPE_1("1","系统接口白名单", BasisConstant.WHITELIST_TYPE,1,"sys","不受角色权限控制,可直接访问"),
        WHITELIST_TYPE_2("2","接口访问时效白名单", BasisConstant.WHITELIST_TYPE,2,"sys","接口连续访问不受重放时间限制"),
        ROLE_STATUS_0("0","正常", BasisConstant.ROLE_STATUS,0,"sys","可用的角色状态"),
        ROLE_STATUS_1("1","停用", BasisConstant.ROLE_STATUS,1,"sys","不可用的角色状态"),
        ROLE_TYPE_0("0","平台角色", BasisConstant.ROLE_TYPE,0,"sys","该角色只用于平台管理系用户使用"),
        ROLE_TYPE_1("1","微信角色", BasisConstant.ROLE_TYPE,1,"sys","该角色只用于微信端用户使用"),
        ROLE_TYPE_2("2","APP角色", BasisConstant.ROLE_TYPE,2,"sys","该角色只用于安卓或IOS客户端用户使用"),
        ROLE_DEFAULT_SELECT_0("0","非默认", BasisConstant.ROLE_DEFAULT_SELECT,0,"sys","注册客户端用户时角色不被系统默认选中"),
        ROLE_DEFAULT_SELECT_1("1","默认", BasisConstant.ROLE_DEFAULT_SELECT,1,"sys","注册客户端用户时角色被系统默认选中"),
        IS_ROLE_PERMISSION_0("0","授予", BasisConstant.IS_ROLE_PERMISSION,0,"sys","授予角色，角色可关联此权限"),
        IS_ROLE_PERMISSION_1("1","不授予", BasisConstant.IS_ROLE_PERMISSION,0,"sys","不授予角色，角色不可关联此权限"),
        SERVICE_MODULES_NAME_0("sys","系统管理端", BasisConstant.SERVICE_MODULES_NAME,0,"all","系统基础建设系统服务"),
        SERVICE_MODULES_NAME_1("auth","认证模块", BasisConstant.SERVICE_MODULES_NAME,1,"all","系统认证服务"),
        ;

        /**
         * 字段key
         */
        private Object dictKey;

        /**
         * 字典值
         */
        private String dictValue;

        /**
         * 字典分组类型
         */
        private String groupType;

        /**
         * 排序字段
         */
        private Integer sort;

        /**
         * 字典模块  all-所有模块
         */
        private String modules;

        /**
         * 字典描述
         */
        private String dictDesc;

        public String getDictKey() {
            return String.valueOf(dictKey);
        }
        public Object getObjDictKey() {
            return dictKey;
        }

        public String getDictValue() {
            return dictValue;
        }

        public String getGroupType() {
            return groupType;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        public String getModules() {
            return modules;
        }

        public String getDictDesc() {
            return dictDesc;
        }

        Dict(Object dictKey,String dictValue,String groupType,Integer sort,String modules,String dictDesc){
            this.dictKey = dictKey;
            this.dictValue = dictValue;
            this.groupType = groupType;
            this.sort = sort;
            this.modules = modules;
            this.dictDesc = dictDesc;
        }

        /**
         * 获取字典集合
         * @return
         */
        public static List<DictVo> getDictList(){
            List<DictVo> dictVos = Lists.newArrayList();
            Arrays.stream(CommonEnumConstant.Dict.values()).forEach(dict -> {
                dictVos.add(DictVo.builder()
                        .dictKey(dict.getObjDictKey())
                        .dictValue(dict.getDictValue())
                        .groupType(dict.getGroupType())
                        .sort(dict.getSort())
                        .modules(dict.getModules())
                        .dictDesc(dict.getDictDesc())
                        .build());
            });
            return dictVos;
        }

        /**
         * 通过分组和 key获取字典值
         * @param dictKey
         * @param groupType
         * @return
         */
        public static String getDictValueByDictKeyAndGroupType(String dictKey,String groupType){
            return Arrays.stream(CommonEnumConstant.Dict.values())
                    .filter(e -> Objects.equals(e.getGroupType(),groupType)&&Objects.equals(e.getDictKey(),dictKey))
                    .findFirst().map(CommonEnumConstant.Dict::getDictValue)
                    .orElse("");
        }
    }

    /**
     * 登入类型
     */
    public enum LoginType {

        LOGIN_TYPE_MOBILE("MOBILE","basicMobileAuthentication","手机号登入"),
        LOGIN_TYPE_EMAIL("EMAIL","basicEmailAuthentication","邮箱登入"),
        LOGIN_TYPE_ACCOUNT("ACCOUNT","basicAccountAuthentication","账号密码登入"),
        OAUTH_LOGIN_TYPE("OAUTH",null,"oauth模式bearer类型登入"),
        ;

        /**
         * 类型key
         */
        private String typeKey;

        /**
         * 适配器 方法
         */
        private String adapter;

        /**
         * 描述
         */
        private String desc;

        LoginType(String typeKey,String adapter,String desc){
            this.typeKey = typeKey;
            this.adapter= adapter;
            this.desc = desc;
        }

        public String getTypeKey() {
            return typeKey;
        }

        public String getDesc() {
            return desc;
        }

        public String getAdapter() {
            return adapter;
        }

        /**
         * 根据类型key获取适配器
         * @param typeKey
         * @return
         */
        public static String getAdapterByTypeKey(String typeKey){
            return Arrays.stream(CommonEnumConstant.LoginType.values())
                    .filter(e -> Objects.equals(e.getTypeKey(),typeKey))
                    .findFirst().map(CommonEnumConstant.LoginType::getAdapter)
                    .orElse(null);
        }
    }
}
