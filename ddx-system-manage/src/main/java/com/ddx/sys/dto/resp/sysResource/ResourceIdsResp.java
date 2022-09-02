package com.ddx.sys.dto.resp.sysResource;

import com.ddx.sys.dto.vo.resource.TreeMenuVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: ResourceIdsResp
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022-08-26 15:56
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="ResourceIdsResp", description="资源菜单ids")
public class ResourceIdsResp {

    @ApiModelProperty(value = "资源ID")
    private List<Long> resourceIds;
}
