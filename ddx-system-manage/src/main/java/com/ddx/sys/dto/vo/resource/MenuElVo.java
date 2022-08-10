package com.ddx.sys.dto.vo.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

/**
 * @ClassName: MenuElVo
 * @Description: 用户菜单元素权限
 * @Author: YI.LAU
 * @Date: 2022年05月23日
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="MenuElVo", description="用户菜单元素权限")
public class MenuElVo {

    @ApiModelProperty(value = "元素key 父级菜单")
    private Long elKey;

    @ApiModelProperty(value = "元素值")
    private Set<String> elValue;
}
