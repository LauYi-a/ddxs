package com.ddx.basis.utils;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName: FileUtil
 * @Description: 文件处理工具类
 * @Author: YI.LAU
 * @Date: 2022年10月31日 14:42
 * @Version: 1.0
 */
public class FileUtil {

    public static String getClassPath(){
        return Class.class.getClass().getResource("/").getPath();
    }

    /**
     * 向本地文件中追加写入内容
     * @param path
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static void localFileWriter(String path,String fileName,String data) throws IOException {
        FileWriter fw = null;
        try {
            File file = new File(createFilePath(path,fileName));
            fw = new FileWriter(file, true);
            fw.write(data);
        }finally {
            if (fw != null){
                fw.close();
            }
        }
    }

    /**
     * 文件目录不存在则创建目录
     * @param path
     * @param fileName
     * @return
     */
    public static String createFilePath(String path,String fileName){
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        return dir+File.separator+fileName;
    }

    /**
     * 获取本地路径下的目录
     * @param path
     * @return
     */
    public static List<String> getLocalPathDir(String path){
        List<String> dirName = Lists.newArrayList();
        File dir = new File(path);
        File[] file = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory()) {
                dirName.add(file[i].getName());
            }
        }
        return dirName;
    }
}
