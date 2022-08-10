package com.ddx.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: PaginatedResult
 * @Description: 分页返回消息体
 * @Author: YI.LAU
 * @Date: 2022年03月25日  0025
 * @Version: 1.0
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResult<T> {

    @ApiModelProperty("当前页")
    private int currentPage;

    @ApiModelProperty("总共页数")
    private int totalPage;

    @ApiModelProperty("总共条数")
    private Long totalCount;

    @ApiModelProperty("返回数据")
    private T resultData;
}
