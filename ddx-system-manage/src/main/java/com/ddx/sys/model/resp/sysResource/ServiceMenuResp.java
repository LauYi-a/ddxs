package com.ddx.sys.model.resp.sysResource;

import com.ddx.sys.model.vo.resource.TreeMenuVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: ServiceMenuResp
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年08月05日 16:41
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="ServiceMenuResp", description="服务资源菜单")
public class ServiceMenuResp {

    @ApiModelProperty(value = "服务编号")
    private String serviceCode;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "资源菜单")
    private List<TreeMenuVo> treeMenuVo;
}
