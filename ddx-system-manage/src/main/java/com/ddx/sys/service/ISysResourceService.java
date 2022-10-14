package com.ddx.sys.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ddx.sys.dto.req.sysResource.SysResourceQueryReq;
import com.ddx.sys.dto.resp.sysResource.MenuTreeListResp;
import com.ddx.sys.dto.resp.sysResource.ServiceMenuResp;
import com.ddx.sys.dto.vo.resource.MenuElVo;
import com.ddx.sys.dto.vo.resource.UserTreeMenuVo;
import com.ddx.sys.entity.SysResource;

import java.util.List;

/**
 * @ClassName: ISysResourceService
 * @Description: 系统资源 服务类
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */
public interface ISysResourceService extends IService<SysResource> {

    /**
     * 分页查询
     * @param arg0
     * @param arg1
     * @return
     */
    IPage<SysResource> selectPage(IPage<SysResource> arg0, Wrapper<SysResource> arg1);

    /**
     * 根据用户资源中间表的资源ID与服务编号查询菜单树
     * @param userResourceIds
     * @param serviceModule
     * @return
     */
    List<UserTreeMenuVo> getMenuTreeByUserAndServiceResourceIds(List<Long> userResourceIds, String serviceModule);

    /**
     * 根据用户资源中间表ID与服务编号查询菜单元素资源
     * @param userResourceIds 用户资源关联ID
     * @param serviceModule 查询服务模块 null 所有模块
     * @param IsComponentOrName Component -> true name ->false
     * IsComponentOrName 标记返回的 MenuElResp 集合 elValue 字段用什么值作为 value
     * @return MenuElResp
     */
    List<MenuElVo> getMenuElByAndServiceResourceIds(List<Long> userResourceIds, String serviceModule, Boolean IsComponentOrName);

    /**
     * 根据用户资源中间表ID 过滤用户有权限的 服务标识
     * @param userResourceIds
     * @return List<String>
     */
    List<String> getServiceList(List<Long> userResourceIds);

    /**
     * 查询资源菜单树
     * @return
     */
    List<ServiceMenuResp> selectMenuTree();

    /**
     * 查询资菜单源树列表
     * @return
     */
    List<MenuTreeListResp> selectMenuTreeList(SysResourceQueryReq sysResourceQueryReq);
}
