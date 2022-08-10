package com.ddx.sys.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: SysAspectLog
 * @Description: 切面日志
 * @author YI.LAU
 * @since 2022-04-29
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_aspect_log")
public class SysAspectLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "全局流水号")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty(value = "请求类型")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "请求接口")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "请求头")
    @TableField("header")
    private String header;

    @ApiModelProperty(value = "请求响应参数")
    @TableField("param")
    private String param;

    @ApiModelProperty(value = "请求IP地址")
    @TableField("ip")
    private String ip;

    @ApiModelProperty(value = "请求用户名称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "请求用户ID")
    @TableField("userId")
    private String userId;

    @ApiModelProperty(value = "创建日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改日期")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
