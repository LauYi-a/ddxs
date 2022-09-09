package com.ddx.basis.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: DeleteKey
 * @Description: 批量删除主键
 * @Author: YI.LAU
 * @Date: 2022年03月28日
 * @Version: 1.0
 */

@Data
@ApiModel(value = "BatchDeleteKey" ,description = "批量删除主键")
public class BatchDeleteKey<T> {

    @ApiModelProperty("批量删除主键")
    @NotNull(message = "批量删除主键不能为空")
    private List<T> keyWords;
}
