package com.ddx.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 全局流水号
     */
    @TableField("serial_number")
    private String serialNumber;

    /**
     * 请求类型
     */
    @TableField("type")
    private String type;

    /**
     * 请求接口
     */
    @TableField("url")
    private String url;

    /**
     * 请求头
     */
    @TableField("header")
    private String header;

    /**
     * 请求响应参数
     */
    @TableField("param")
    private String param;

    /**
     * 请求IP地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 请求用户名称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 请求用户ID
     */
    @TableField("userId")
    private String userId;

    /**
     * 创建日期
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
