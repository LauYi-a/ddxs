package com.ddx.sys.model.resp.sysResource;

import com.ddx.sys.model.vo.resource.MenuElVo;
import com.ddx.sys.model.vo.resource.UserTreeMenuVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: TreeAndAuthElResp
 * @Description: 用户菜单与用户元素权限
 * @Author: YI.LAU
 * @Date: 2022年05月23日  0023
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="TreeMenuAndElAuthResp", description="用户菜单与用户元素权限返回体")
public class TreeMenuAndElAuthResp {

    @ApiModelProperty(value = "用户资源菜单集合")
    List<UserTreeMenuVo> treeMenu;

    @ApiModelProperty(value = "用户页面元素权限")
    List<MenuElVo> menuEl;

    @ApiModelProperty("用户所有服务")
    List<String> serviceList;
}
