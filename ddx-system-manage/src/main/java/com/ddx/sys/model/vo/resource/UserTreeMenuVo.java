package com.ddx.sys.model.vo.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: TreeMenuVo
 * @Description: 用户资源参数
 * @Author: YI.LAU
 * @Date: 2022年05月11日
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="UserTreeMenuVo", description="用户资源菜单参数")
public class UserTreeMenuVo {

    @ApiModelProperty(value = "资源路径")
    private String path;

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "使用组件")
    private String component;

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "重定向地址")
    private String redirect;

    @ApiModelProperty(value = "菜单是否隐藏菜单栏 false 不隐藏 true 隐藏 ")
    private Boolean hideMenu;

    @ApiModelProperty(value = "菜单其他属性")
    private MenuMetaVo meta;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @JsonInclude(value= JsonInclude.Include.NON_EMPTY)
    @ApiModelProperty(value = "菜单子集")
    List<UserTreeMenuVo> children;
}
