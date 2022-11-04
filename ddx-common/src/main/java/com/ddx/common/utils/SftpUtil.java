package com.ddx.common.utils;

import com.ddx.basis.exception.ExceptionUtils;
import com.ddx.common.config.SftpConfig;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

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
     * 获得 session
     * @return
     */
    public static Session getSession(SftpConfig sftpConfig){
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(sftpConfig.getUsername(),sftpConfig.getHost(),sftpConfig.getPort());
            sshSession.setPassword(sftpConfig.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.setTimeout(30000);
            return sshSession;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 连接SFTP服务器
     */
    public static ChannelSftp connectSftp(Session sshSession) {
        try {
            if (sshSession == null){
                log.error("Session 获取失败");
                return null;
            }
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;
            return sftp;
        } catch (Exception e) {
            log.error("创建sftp连接失败：{}",e.getMessage());
            return null;
        }
    }

    /**
     * 关闭连接
     */
    public static void disconnect(ChannelSftp sftp,Session sshSession) {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (sshSession != null) {
            if (sshSession.isConnected()) {
                sshSession.disconnect();
            }
        }
    }

    /**
     * 进入 sftp 目录 不存在则创建目录
     * @param sftp
     * @param path
     */
    public static void cdSftpPath(ChannelSftp sftp, String path){
        try {
            sftp.cd(path);
        }catch (Exception e){
            try {
                sftp.mkdir(path);
            }catch (Exception e1){
                ExceptionUtils.errorBusinessException(sftp == null,"SFTP 创建目录失败");
            }
        }
    }
}
