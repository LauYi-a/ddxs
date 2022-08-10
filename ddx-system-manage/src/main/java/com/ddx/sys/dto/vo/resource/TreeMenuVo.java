package com.ddx.sys.dto.vo.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @ClassName: TreeMenuVo
 * @Description: 资源菜单树
 * @Author: YI.LAU
 * @Date: 2022年08月05日 16:25
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="TreeMenuVo", description="资源菜单树")
public class TreeMenuVo {

    @ApiModelProperty(value = "资源菜单主键")
    private String id;

    @ApiModelProperty(value = "资源菜单标签")
    private String label;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "子菜单")
    private List<TreeMenuVo> children;
}
