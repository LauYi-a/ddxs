package com.ddx.sys.model.vo.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: MenuMetaResp
 * @Description: 菜单Meta参数
 * @Author: YI.LAU
 * @Date: 2022年05月12日
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="MenuMetaVo", description="菜单Meta参数")
public class MenuMetaVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "根节点")
    private Long rootId;

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "父节点")
    private Long parentId;

    @ApiModelProperty(value = "资源名称")
    private String title;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "资源类型  0 页签 1 菜单 2 元素")
    private String resourceType;

    @ApiModelProperty(value = "是否外链 N否  Y 是")
    private String isExternal;

    @ApiModelProperty(value = "是否缓存")
    private Boolean cache;

    @ApiModelProperty(value = "是否显示tabs标签 false 不显示 true 显示")
    private Boolean hideTabs;

    @ApiModelProperty(value = "是否可以关闭tabs标签 false 不关闭 true 关闭")
    private Boolean hideClose;

}
