package com.ddx.common.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: PageReq
 * @Description: 分页入参体
 * @Author: YI.LAU
 * @Date: 2022年03月25日
 * @Version: 1.0
 */
@Data
@ApiModel(value = "PageReq" ,description = "分页入参体")
public class PageReq {

    @ApiModelProperty("当前页")
    @NotNull(message = "当前页不能为空")
    private String page;

    @ApiModelProperty("显示页")
    @NotNull(message = "显示页不能为空")
    private String perPage;
}
