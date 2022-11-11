package com.ddx.util.basis.utils;

import com.ddx.util.basis.config.SftpConfig;
import com.ddx.util.basis.enums.CommonEnumConstant;
import com.ddx.util.basis.exception.ExceptionUtils;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Arrays;

/**
 * @ClassName: SFTPUtils
 * @Description:  SFTP 工具类
 * @Author: YI.LAU
 * @Date: 2022年11月02日 17:36
 * @Version: 1.0
 */
@Slf4j
public class SftpUtil {

    /**
     * 获得文件的输入流
     * @param filePath
     * @param channelSftp
     * @return
     */
    public InputStream readFileInputStream(String filePath,ChannelSftp channelSftp){
        try {
            return channelSftp.get(filePath);
        } catch (SftpException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断目录是否存在
     * @param directory
     * @return
     */
    public static boolean isExist(String directory,ChannelSftp channelSftp) {
        try {
            SftpATTRS sftpATTRS = channelSftp.lstat(directory);
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                return false;
            }
            return false;
        }
    }

    /**
     * 进入目录
     * @param dir
     * @param sftp
     * @return
     */
    public static boolean cdDir(String dir, ChannelSftp sftp){
        try {
            sftp.cd(dir);
            log.info("cd directory {}", dir);
            return true;
        } catch (Exception e) {
            log.error("cd directory failure, directory:{}", dir, e);
           return false;
        }
    }

    /**
     * 创建并进入目录
     * @param dirPath
     * @param sftp
     * @return
     */
    public static boolean createAndCdDirs(String dirPath, ChannelSftp sftp) {
        if (StringUtils.isNotBlank(dirPath)) {
            if (isExist(dirPath,sftp)){
                cdDir(dirPath,sftp);
                return true;
            }
            String[] dirs = Arrays.stream(dirPath.split("/")).filter(StringUtils::isNotBlank).toArray(String[]::new);
            for (String dir : dirs) {
                try {
                    sftp.cd(dir);
                    log.info("cd directory {}", dir);
                } catch (Exception e) {
                    try {
                        sftp.mkdir(dir);
                        log.info("mkdir directory {}", dir);
                    } catch (SftpException e1) {
                        log.error("mkdir directory failure, directory:{}", dir, e1);
                        e1.printStackTrace();
                        return false;
                    }
                    try {
                        sftp.cd(dir);
                        log.info("cd directory {}", dir);
                    } catch (SftpException e1) {
                        log.error("cd directory failure, directory:{}", dir, e1);
                        e1.printStackTrace();
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 登录SFTP
     * @return
     */
    public static ChannelSftp loginSftp(SftpConfig sftpConfig) {
        ChannelSftp channelSftp = null;
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(sftpConfig.getUsername(),sftpConfig.getHost(),sftpConfig.getPort());
            sshSession.setPassword(sftpConfig.getPassword());
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.connect(30000);
            Channel channel = sshSession.openChannel("sftp");
            channel.connect(30000);
            channelSftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            e.printStackTrace();
            ExceptionUtils.businessException(CommonEnumConstant.PromptMessage.FAILED,String.format("登入SFTP失败：%s",e.getMessage()));
        }
        return channelSftp;
    }

    /**
     * 关闭SFTP
     * @param channelSftp
     */
    public static void logoutSftp(ChannelSftp channelSftp) {
        try {
            if( channelSftp == null){
                return;
            }
            if(channelSftp.isConnected()){
                channelSftp.disconnect();
            }
            if(channelSftp.getSession() != null){
                channelSftp.getSession().disconnect();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
