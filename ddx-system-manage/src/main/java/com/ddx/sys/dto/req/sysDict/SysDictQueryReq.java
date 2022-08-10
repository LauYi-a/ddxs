package com.ddx.sys.dto.req.sysDict;

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
 * @ClassName: SysDictQueryReq
 * @Description: 字典查询入参体
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Data
@ApiModel(value="SysDictQueryReq", description="查询入参体")
public class SysDictQueryReq extends PageReq {

    @ApiModelProperty(value = "字典key")
    private String key;

    @ApiModelProperty(value = "字典分组类型")
    private String groupType;

    @ApiModelProperty(value = "字典模块  all-所有模块")
    private String modules;

    @ApiModelProperty(value = "字典描述")
    private String desc;

    @ApiModelProperty(value = "字典值")
    private String value;

}
