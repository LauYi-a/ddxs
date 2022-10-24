package com.ddx.basis.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: SysLogAspectVo
 * @Description: 系统请求日志切面
 * @Author: YI.LAU
 * @Date: 2022年04月27日
 * @Version: 1.0
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLogAspectVo<T> {

    /**
     * 请求头
     */
    @JsonProperty(index = 1)
    private Header header;

    /**
     * 请求或返回参数
     */
    @JsonProperty(index = 2)
    private T data;



}
