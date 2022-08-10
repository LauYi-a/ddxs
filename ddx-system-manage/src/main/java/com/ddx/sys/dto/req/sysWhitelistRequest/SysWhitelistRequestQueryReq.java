package com.ddx.sys.dto.req.sysWhitelistRequest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.ddx.common.dto.req.PageReq;

/**
 * @ClassName: SysWhitelistRequestQuotaReq
 * @Description: 白名单路由查询入参体
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysWhitelistRequestQuotaReq", description="白名单路由查询入参体")
public class SysWhitelistRequestQueryReq extends PageReq {

    @ApiModelProperty(value = "白名单路由名称")
    private String name;

    @ApiModelProperty(value = "白名单路由")
    private String url;

    @ApiModelProperty(value = "服务模块 sys-系统服务 auth-认证服务")
    private String serviceModule;

}
