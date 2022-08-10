package com.ddx.common.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: KeyWord
 * @Description: 模糊查询键
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */
@Data
@ApiModel(value = "PageReq" ,description = "模糊查询键")
public class KeyWord<T> extends PageReq {

    @ApiModelProperty("查询键")
    private T KeyWordName;
}
