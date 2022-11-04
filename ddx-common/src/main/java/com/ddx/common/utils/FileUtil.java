package com.ddx.common.utils;

import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.common.config.SftpConfig;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @ClassName: FileUtil
 * @Description: 文件处理工具类
 * @Author: YI.LAU
 * @Date: 2022年10月31日 14:42
 * @Version: 1.0
 */
public class FileUtil {

    public static void connectSftpWriterFile(SftpConfig sftpConfig, String path, String fileName, String data){
        Session session = null;
        ChannelSftp sftp = null;
        try {
            session = SftpUtil.getSession(sftpConfig);
            sftp = SftpUtil.connectSftp(session);
            ExceptionUtils.errorBusinessException(sftp == null,"SFTP 连接失败");
            SftpUtil.cdSftpPath(sftp,path);
            fileWriter(path,fileName,data);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            SftpUtil.disconnect(sftp,session);
        }
    }

    /**
     * 向文件中追加写入内容
     * @param path
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static void fileWriter(String path,String fileName,String data) throws IOException {
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
}
