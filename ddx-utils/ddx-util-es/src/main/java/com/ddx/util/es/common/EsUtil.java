package com.ddx.util.es.common;

import com.ddx.util.es.annotation.DocId;
import com.ddx.util.es.annotation.EsClass;
import com.ddx.util.es.annotation.EsEnum;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: EsUtil
 * @Description: ES 工具类
 * @Author: YI.LAU
 * @Date: 2022年11月16日 10:14
 * @Version: 1.0
 */
public class EsUtil {

    /**
     * 获取注解的索引名
     * 前缀+索引
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> String getIndex(Class<T>  clazz,String IndexOrAliasPrefix,String indexOrAliasName){
        if (Objects.nonNull(indexOrAliasName)&&""!=indexOrAliasName){
            upIndexAndAlias(clazz,indexOrAliasName);
        }
        EsClass annotation = clazz.getAnnotation(EsClass.class);
        String index = Objects.equals(IndexOrAliasPrefix,EsEnum.EsIndexPrefix.INDEX_PREFIX.getPrefix())?annotation.index():annotation.alias();
        index = "".equals(index) ? Objects.requireNonNull(clazz.getSimpleName().toLowerCase()) : index.toLowerCase();
        return IndexOrAliasPrefix+index;
    }

    /**
     * 自定义索引
     * @param clazz
     * @param <T>
     */
    public static <T> void upIndexAndAlias(Class<T>  clazz,String indexName){
        EsClass esClass = clazz.getAnnotation(EsClass.class);
        try {
            InvocationHandler h = Proxy.getInvocationHandler(esClass);
            Field hField = h.getClass().getDeclaredField("memberValues");
            hField.setAccessible(true);
            Map memberValues = (Map) hField.get(h);
            memberValues.put("index", indexName);
            memberValues.put("alias", indexName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 ID
     * @param o
     * @param <T>
     * @return
     */
    public static <T> Object getId(T o){
        Object id = null;
        for (Field declaredField : o.getClass().getDeclaredFields()) {
            declaredField.setAccessible(true);
            DocId annotation = declaredField.getAnnotation(DocId.class);
            if (annotation != null) {
                try {
                    id =  declaredField.get(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return id;
    }

    /**
     * 根据键值对填充字符串，如("hello ${name}",{name:"xiaoming"})
     * 输出：
     * @param content
     * @param map
     * @return
     */
    public static String renderString(String content, Map<String, String> map){
        Set<Map.Entry<String, String>> sets = map.entrySet();
        for(Map.Entry<String, String> entry : sets) {
            String regex = "\\$\\{" + entry.getKey() + "\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll(entry.getValue());
        }
        return content;
    }
}
