package com.ddx.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: LoginVal
 * @Description: 保存登录用户的信息，此处可以根据业务需要扩展
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginVal extends JwtInformation{

    private String userId;

    private String username;

    private String nickname;

    private String[] authorities;
}
