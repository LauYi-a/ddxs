package com.ddx.basis.enums;

import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.dto.vo.DictVo;
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
        SUCCESS(ConstantUtils.MSG_RESPONSE_CODE_INFO, ConstantUtils.MSG_TYPE_SUCCESS,"操作成功"),
        LOING_SUCCESS(ConstantUtils.MSG_RESPONSE_CODE_INFO, ConstantUtils.MSG_TYPE_SUCCESS,"登入成功"),
        REVOKE_TOKEN_YES(ConstantUtils.MSG_RESPONSE_CODE_INFO,ConstantUtils.MSG_TYPE_SUCCESS,"注销成功"),

        /**
         * 警告类信息提示
         */
        VALIDATED_FAILED(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"未查询到原数据，验证失败"),
        REDIS_NOT_RESOURCES_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_WARNING,"缓存中未获取到访问资源"),
        USER_NOT_FOUND_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户未注册"),
        USER_USERNAME_EXISTING_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"账号已注册"),
        USER_MOBILE_EXISTING_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"手机号码已使用"),
        QUOTA_USER_NOT_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"未找到用户"),
        USER_DISABLE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户已禁用"),
        USER_LOCK_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"账户已锁定"),
        USER_DISABLE_TIME_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"账户已锁定，请 %s 秒后重试"),
        USERNAME_OR_PASSWORD_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户名或密码错误"),
        USER_OLD_PASSWORD_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户旧密码错误"),
        USER_NEW_PASSWORD_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"新密码不一致"),
        IN_BUSINESS_PROCESS_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"业务处理中"),
        REDIS_LOCK_KEY_ISNULL_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"分布式锁KEY为空"),
        USER_MENU_ISNULL_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"用户未分配可用菜单"),
        NO_TOKEN(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"无TOKEN信息"),
        ROLE_CODE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_WARNING,"角色编号已存在"),
        /**
         * 错误类信息提示
         */
        REVOKE_TOKEN_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_WARNING,"注销失败"),
        FAILED(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"操作失败"),
        NO_PERMISSION(ConstantUtils.MSG_RESPONSE_CODE_NO_PERMISSION,ConstantUtils.MSG_TYPE_ERROR,"无权限访问"),
        CLIENT_AUTHENTICATION_FAILED(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"客户端认证失败"),
        CLIENT_FAILED(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"客户端连接失败"),
        ID_AUTHENTICATION_FAILED(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"身份验证失败"),
        INIT_ROLE_PERMISSION_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"初始化角色权限失败"),
        INIT_USER_KEY_VAL_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"初始化用户键值失败"),
        INIT_WHITELIST_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"初始化白名单失败"),
        SYS_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"服务异常,错误信息为：%s"),
        SYS_NULL_POINTER_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"系统空指针异常"),
        API_READ_TIMED_OUT_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"接口超时请稍后再试，超时接口：%s"),
        BUSINSEE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_ERROR,"业务异常"),
        KEY_STRING_INDEX_OUT_OF_BOUNDS_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR, ConstantUtils.MSG_TYPE_WARNING,"密钥字节长度越界，默认只支持 %s 位长度密钥"),
        UNSUPPORTED_GRANT_TYPE(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"不支持的认证模式"),
        PARAMETER_ILLEGAL_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"非法参数"),
        ILLEGAL_REQUEST_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"非法请求"),
        INTERNAL_SERVER_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"服务繁忙"),
        FREQUENT_RESPONSE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_ERROR,ConstantUtils.MSG_TYPE_ERROR,"请求繁忙"),
        INVALID_TOKEN(ConstantUtils.MSG_RESPONSE_CODE_INVALID_TOKEN,ConstantUtils.MSG_TYPE_ERROR,"TOKEN无效或过期"),
        ADD_ROLE_PERMISSION_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_ERROR,"绑定角色权限失败"),
        DELETE_ROLE_PERMISSION_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_ERROR,"删除角色权限失败"),
        ADD_USER_ROLE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_ERROR,"绑定用户角色失败"),
        DELETE_USER_ROLE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_ERROR,"删除用户角色失败"),
        ADD_USER_RESOURCE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_ERROR,"绑定用户资源失败"),
        DELETE_USER_RESOURCE_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING, ConstantUtils.MSG_TYPE_ERROR,"删除用户资源失败"),
        KAFKA_SEND_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_ERROR,"发送消息失败: %s"),
        KAFKA_CONSUMER_ERROR(ConstantUtils.MSG_RESPONSE_CODE_WARNING,ConstantUtils.MSG_TYPE_ERROR,"消费消息失败: %s"),

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

        USER_GENDER_0("1","男",ConstantUtils.USER_GENDER,0,"sys","用户性别"),
        USER_GENDER_1("2","女",ConstantUtils.USER_GENDER,1,"sys","用户性别"),
        USER_STATUS_0("0","禁用",ConstantUtils.USER_STATUS,0,"sys","用户禁止使用"),
        USER_STATUS_1("1","正常",ConstantUtils.USER_STATUS,1,"sys","用户可正常使用"),
        USER_STATUS_2("2","已锁",ConstantUtils.USER_STATUS,2,"sys","用户已被锁定"),
        MENU_TYPE_0("0","页签",ConstantUtils.MENU_TYPE,0,"sys","菜单类型"),
        MENU_TYPE_1("1","菜单",ConstantUtils.MENU_TYPE,1,"sys","菜单类型"),
        MENU_TYPE_2("2","元素",ConstantUtils.MENU_TYPE,2,"sys","菜单类型"),
        WHITELIST_TYPE_0("1","接口访问白名单",ConstantUtils.WHITELIST_TYPE,0,"sys","不受角色权限控制"),
        WHITELIST_TYPE_1("2","接口访问时效白名单",ConstantUtils.WHITELIST_TYPE,1,"sys","接口连续反问不受限制"),
        ROLE_STATUS_0("0","正常",ConstantUtils.ROLE_STATUS,0,"sys","可用的角色状态"),
        ROLE_STATUS_1("1","停用",ConstantUtils.ROLE_STATUS,1,"sys","不可用的角色状态"),
        SERVICE_MODULES_NAME_0("sys","系统基础应用",ConstantUtils.SERVICE_MODULES_NAME,0,"all","系统服务模块名称"),
        SERVICE_MODULES_NAME_1("auth","认证服务",ConstantUtils.SERVICE_MODULES_NAME,1,"all","系统认证模块名称"),
        ;

        /**
         * 字段key
         */
        private String dictKey;

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
            return dictKey;
        }

        public void setDictKey(String dictKey) {
            this.dictKey = dictKey;
        }

        public String getDictValue() {
            return dictValue;
        }

        public void setDictValue(String dictValue) {
            this.dictValue = dictValue;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
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

        public void setModules(String modules) {
            this.modules = modules;
        }

        public String getDictDesc() {
            return dictDesc;
        }

        public void setDictDesc(String dictDesc) {
            this.dictDesc = dictDesc;
        }

        Dict(String dictKey,String dictValue,String groupType,Integer sort,String modules,String dictDesc){
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
                        .dictKey(dict.getDictKey())
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

}
