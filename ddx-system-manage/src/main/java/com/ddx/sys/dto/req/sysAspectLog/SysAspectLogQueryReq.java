package com.ddx.sys.dto.req.sysAspectLog;

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
 * @ClassName: SysAspectLogQueryReq
 * @Description: 切面日志查询入参体
 * @author YI.LAU
 * @since 2022-04-29
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysAspectLogQueryReq", description="切面日志查询入参体")
public class SysAspectLogQueryReq extends PageReq {

    @ApiModelProperty(value = "全局流水号")
    private String serialNumber;

    @ApiModelProperty(value = "请求接口")
    private String url;

    @ApiModelProperty(value = "请求用户ID")
    private String userId;

}
