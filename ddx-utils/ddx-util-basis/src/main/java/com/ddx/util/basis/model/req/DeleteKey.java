package com.ddx.util.basis.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: DeleteKey
 * @Description: 删除主键
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */

@Data
@ApiModel(value = "DeleteKey" ,description = "删除主键")
public class DeleteKey<T> {

    @ApiModelProperty("删除主键")
    @NotNull(message = "删除主键不能为空")
    private T keyWord;
}
