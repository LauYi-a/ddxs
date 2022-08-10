package com.ddx.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: SysWhitelistRequest
 * @Description: 白名单路由
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_whitelist_request")
public class SysWhitelistRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "白名单路由名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "白名单路由")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "请求白名单类型 1 接口访问 2接口访问时间")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "服务模块 sys-系统服务 auth-认证服务")
    @TableField("service_module")
    private String serviceModule;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者id")
    @TableField(value = "create_id", fill = FieldFill.INSERT)
    private Long createId;

    @ApiModelProperty(value = "修改者id")
    @TableField(value = "update_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateId;


}
