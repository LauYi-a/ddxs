package com.ddx.sys.dto.req.sysDict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: SysDictCheckKey
 * @Description: 字典校验参数入参
 * @Author: YI.LAU
 * @Date: 2022年04月26日  0026
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysDictCheckKey", description="字典校验参数入参")
public class SysDictCheckKey {

    @ApiModelProperty(value = "字典分组类型")
    @NotBlank(message = "字典分组类型不能为空")
    private String groupType;

    @ApiModelProperty(value = "字典模块  all-所有模块")
    @NotBlank(message = "字典模块  all-所有模块不能为空")
    private String modules;

    @ApiModelProperty(value = "字典key")
    @NotBlank(message = "字典key不能为空")
    private String key;

    @ApiModelProperty(value = "字典值")
    @NotBlank(message = "字典值不能为空")
    private String value;
}
