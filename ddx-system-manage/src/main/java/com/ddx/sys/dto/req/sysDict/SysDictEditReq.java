package com.ddx.sys.dto.req.sysDict;

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
 * @ClassName: SysDictEditReq
 * @Description: 字典编辑入参体
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysDictEditReq", description="编辑入参体")
public class SysDictEditReq {

    @ApiModelProperty(value = "主键")
    @NotNull(message = "主键不能为空")
    private Long id;

    @ApiModelProperty(value = "字典key")
    @NotBlank(message = "字典key不能为空")
    private String key;

    @ApiModelProperty(value = "字典值")
    @NotBlank(message = "字典值不能为空")
    private String value;

    @ApiModelProperty(value = "字典分组类型")
    @NotBlank(message = "字典分组类型不能为空")
    private String groupType;

    @ApiModelProperty(value = "排序字段")
    @NotNull(message = "排序字段不能为空")
    private Long sort;

    @ApiModelProperty(value = "字典模块  all-所有模块")
    @NotBlank(message = "字典模块  all-所有模块不能为空")
    private String modules;

    @ApiModelProperty(value = "字典描述")
    @NotBlank(message = "字典描述不能为空")
    private String desc;
}
