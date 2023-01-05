package com.ddx.util.basis.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: SysParamConfigVo
 * @Description: 系统参数配置
 * @Author: YI.LAU
 * @Date: 2022年04月06日  0006
 * @Version: 1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="SysParamConfigVo", description="系统参数配置")
public class SysParamConfigVo {

    @ApiModelProperty("登入密码错误次数限制 - 次")
    @NotNull(message = "登入密码错误次数限制不能为空")
    private Integer lpec;

    @ApiModelProperty("账户错误次数后锁定时间 - 秒")
    @Max(value = 3600,message = "账户错误次数后锁定时间最大3600秒")
    @Min(value = 10,message = "账户错误次数后锁定时间最小10秒")
    @NotNull(message = "账户错误次数后锁定时间不能为空")
    private  Long accountLockTime;

    @ApiModelProperty("token 有效期时间 - 小时")
    @Max(value = 72,message = "token 有效期时间最大72小时")
    @Min(value = 1,message = "token 有效期时间最小1小时")
    @NotNull(message = "token 有效期时间不能为空")
    private Long accessTokenTime;

    @ApiModelProperty("刷新 token 有效期时间 - 天")
    @Max(value = 7,message = "刷新 token 有效期时间最大7天")
    @Min(value = 1,message = "刷新 token 有效期时间最小1天")
    @NotNull(message = "刷新 token 有效期时间不能为空")
    private Long refreshTokenTime;

    @ApiModelProperty("系统请求间隔时间 - 秒")
    @Max(value = 120,message = "系统请求间隔时间最大120秒")
    @Min(value = 5,message = "系统请求间隔时间最小5秒")
    @NotNull(message = "系统请求间隔时间不能为空")
    private Long sysRequestTime;

    @ApiModelProperty("网关令牌过期时间 - 秒")
    @Max(value = 3600,message = "网关令牌过期时间最大3600秒")
    @Min(value = 5,message = "网关令牌过期时间最小5秒")
    @NotNull(message = "网关令牌过期时间不能为空")
    private Long gatewayTokenExpireTime;

    @ApiModelProperty("验证码过期时间 - 秒")
    @Max(value = 1800,message = "验证码过期时间最大1800秒")
    @Min(value = 60,message = "验证码过期时间最小60秒")
    @NotNull(message = "验证码过期时间不能为空")
    private Long verificationCodeExpireTime;
}
