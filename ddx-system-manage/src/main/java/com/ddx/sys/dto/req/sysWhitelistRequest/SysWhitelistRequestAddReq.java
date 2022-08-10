package com.ddx.sys.dto.req.sysWhitelistRequest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;

/**
 * @ClassName: SysWhitelistRequestAddReq
 * @Description: 白名单路由新增入参体
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysWhitelistRequestAddReq", description="白名单路由新增入参体")
public class SysWhitelistRequestAddReq {

    @ApiModelProperty(value = "白名单路由名称")
    @NotBlank(message = "白名单路由名称不能为空")
    private String name;

    @ApiModelProperty(value = "白名单路由")
    @NotBlank(message = "白名单路由不能为空")
    private String url;

    @ApiModelProperty(value = "服务模块 sys-系统服务 auth-认证服务")
    @NotBlank(message = "服务模块 sys-系统服务 auth-认证服务不能为空")
    private String serviceModule;

}
