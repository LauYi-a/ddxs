package com.ddx.common.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: CheckKey
 * @Description: 校验参数
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */

@Data
@ApiModel(value = "CheckKey" ,description = "校验参数")
public class CheckKey<T> {

    @ApiModelProperty("校验参数")
    @NotNull(message = "校验参数不能为空")
    private T KeyWord;
}
