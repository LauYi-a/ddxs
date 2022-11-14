package com.ddx.util.basis.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: StringUtil
 * @Description: 字符串工具类
 * @Author: YI.LAU
 * @Date: 2022年04月08日  0008
 * @Version: 1.0
 */
public class StringUtil {

    /**
     * 对url进行校验匹配
     * @param urls 路径集合
     * @param path 对比路径
     * @return true 存在 false 不存在
     */
    public static Boolean checkUrls(List<String> urls, String path){
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String url : urls) {
            if (pathMatcher.match(url,path))
                return true;
        }
        return false;
    }

    /**
     * Json 格式化
     * @param uglyJSONString 被格式化数据
     * @return
     */
    public static String jsonFormatter(Object uglyJSONString){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(JSONObject.toJSONString(uglyJSONString));
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    /**
     * Json 格式化
     * @param uglyJSONString 被格式化数据
     * @param isFormatter 是否格式化
     * @return json
     */
    public static String jsonFormatter(Object uglyJSONString,Boolean isFormatter){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        String prettyJsonString = "";
        if (isFormatter){
            JsonElement je = jp.parse(JSONObject.toJSONString(uglyJSONString));
            prettyJsonString = gson.toJson(je);
        }else{
            prettyJsonString =  JSONObject.toJSONString(uglyJSONString);
        }
        return prettyJsonString;
    }

    /**
     * 获取一个32位的UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
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
