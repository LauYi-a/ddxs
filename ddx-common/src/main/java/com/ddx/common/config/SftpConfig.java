package com.ddx.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: SftpVO
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年11月03日 11:44
 * @Version: 1.0
 */
@Data
@Configuration
public class SftpConfig {

    @Value("${sftp.host:}")
    private String host;

    @Value("${sftp.username:}")
    private String username;

    @Value("${sftp.password:}")
    private String password;

    @Value("${sftp.port:}")
    private int port;
}
