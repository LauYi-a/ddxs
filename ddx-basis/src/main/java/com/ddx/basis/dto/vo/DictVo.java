package com.ddx.basis.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: SysDict
 * @Description: 
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DictVo {

    /**
     * 字典key
     */
    private String dictKey;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 字典分组类型
     */
    private String groupType;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 字典模块  all-所有模块
     */
    private String modules;

    /**
     * 字典描述
     */
    private String dictDesc;

}
