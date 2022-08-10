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
 * @ClassName: SysDict
 * @Description: 
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
@TableName("sys_dict")
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "字典key")
    @TableField("dict_key")
    private String dictKey;

    @ApiModelProperty(value = "字典值")
    @TableField("dict_value")
    private String dictValue;

    @ApiModelProperty(value = "字典分组类型")
    @TableField("group_type")
    private String groupType;

    @ApiModelProperty(value = "排序字段")
    @TableField("sort")
    private Long sort;

    @ApiModelProperty(value = "字典模块  all-所有模块")
    @TableField("modules")
    private String modules;

    @ApiModelProperty(value = "字典描述")
    @TableField("dict_desc")
    private String dictDesc;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者ID")
    @TableField(value = "create_id",fill = FieldFill.INSERT)
    private Long createId;

    @ApiModelProperty(value = "更新者ID")
    @TableField(value = "update_id",fill = FieldFill.INSERT_UPDATE)
    private Long updateId;


}
