package com.ddx.util.log.mc.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName: ConfigFileUtil
 * @Description: 获取配置文件工具类
 * @Author: YI.LAU
 * @Date: 2022年11月10日 16:49
 * @Version: 1.0
 */
public class ConfigFileUtil {

    public static String getClassPath(){
        return Class.class.getClass().getResource("/").getPath();
    }

    /**
     * 读取配置文件内容并转实体后返回
     * @param fileName
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T readConfigJsonFile(String fileName,Class<T> tClass){
        try {
            File file = new File(getClassPath()+fileName);
            String s = FileUtils.readFileToString(file, "utf-8");
            T t = JSON.parseObject(s,tClass);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 写入Json 配置文件
     * 有被覆盖的风险
     * @param fileName
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean writeConfigJsonFile(String fileName, T t){
        try {
            String rootPath = isExists(getClassPath(),fileName);
            File file = new File(rootPath);
            FileUtils.writeStringToFile(file,JSON.toJSONString(t, true),"utf-8");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 判断目录文件是否存在不存在则创建
     * @param path
     * @param fileName
     * @return
     */
    public static String isExists(String path,String fileName) throws IOException {
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File fileNamePath = new File(dir+File.separator+fileName);
        if (!fileNamePath.exists()){
            fileNamePath.createNewFile();
        }
        return dir+File.separator+fileName;
    }

    public static Boolean isConfigFile(String logMcPath,String fileName){
        File fileNamePath = new File(logMcPath+File.separator+fileName);
        if (!fileNamePath.exists()){
            return false;
        }
        return true;
    }
}
