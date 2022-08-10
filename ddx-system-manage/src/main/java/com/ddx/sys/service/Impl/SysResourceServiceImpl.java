package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.sys.dto.resp.sysResource.ServiceMenuResp;
import com.ddx.sys.dto.vo.resource.TreeMenuVo;
import com.ddx.sys.dto.vo.resource.MenuElVo;
import com.ddx.sys.dto.vo.resource.MenuMetaVo;
import com.ddx.sys.dto.vo.resource.UserTreeMenuVo;
import com.ddx.sys.entity.SysResource;
import com.ddx.sys.mapper.SysResourceMapper;
import com.ddx.sys.service.ISysResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: SysResourceServiceImpl
 * @Description: 系统资源 服务实现类
 * @author YI.LAU
 * @since 2022-04-13
 * @Version: 1.0
 */
@Service
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {

    @Override
    public IPage<SysResource> selectPage(IPage<SysResource> arg0,  Wrapper<SysResource> arg1){
        return this.baseMapper.selectPage(arg0, arg1);
    }

    @Override
    public List<UserTreeMenuVo> getMenuTreeByUserAndServiceResourceIds(List<Long> userResourceIds, String serviceModule) {
        List<SysResource> sysResourceList = baseMapper.selectList(new QueryWrapper<SysResource>().lambda().in(SysResource::getId,userResourceIds).eq(StringUtils.isNoneBlank(serviceModule),SysResource::getServiceModule,serviceModule));
        //查询所有用户的一级页签根菜单
        List<SysResource> oneResource= sysResourceList.stream().filter(e -> e.getResourceType().equals(ConstantUtils.MENU_TYPE_0)).collect(Collectors.toList());
        List<UserTreeMenuVo> treeMenuResps = new ArrayList<>();
        //为一级页签根菜单设置子菜单
        oneResource.forEach(menu ->{
            treeMenuResps.add(UserTreeMenuVo.builder()
                .path(menu.getResourceUrl())
                .component(menu.getComponent())
                .hideMenu(menu.getHideMenu())
                .redirect(menu.getRedirect())
                .sort(menu.getSort())
                .meta(MenuMetaVo.builder()
                    .id(menu.getId())
                    .rootId(menu.getRootId())
                    .parentId(menu.getParentId())
                    .resourceType(menu.getResourceType())
                    .title(menu.getResourceName())
                    .icon(menu.getIcon())
                    .isExternal(menu.getIsExternal())
                    .cache(menu.getCache())
                    .hideClose(menu.getHideClose())
                    .hideTabs(menu.getHideTabs())
                    .build())
                .children(getUserMenuChildren(sysResourceList, menu.getId()))
                .build());
        });
        //升序排序
        Collections.sort(treeMenuResps, Comparator.comparing(UserTreeMenuVo::getSort));
        return treeMenuResps;
    }

    @Override
    public List<MenuElVo> getMenuElByAndServiceResourceIds(List<Long> userResourceIds, String serviceModule, Boolean IsComponentOrName) {
        List<SysResource> sysResourceList = baseMapper.selectList(new QueryWrapper<SysResource>().lambda().in(SysResource::getId,userResourceIds).eq(StringUtils.isNoneBlank(serviceModule),SysResource::getServiceModule,serviceModule));
        List<SysResource> elResource= sysResourceList.stream().filter(e -> e.getResourceType().equals(ConstantUtils.MENU_TYPE_2)).collect(Collectors.toList());
        List<MenuElVo> menuElRespList = Lists.newArrayList();
        if (IsComponentOrName){
            elResource.stream().collect(Collectors.groupingBy(SysResource::getParentId, Collectors.mapping(SysResource::getComponent, Collectors.toSet())))
            .forEach((key,val)->{
                menuElRespList.add(MenuElVo.builder().elKey(key).elValue(val).build());
            });
        }else{
            elResource.stream().collect(Collectors.groupingBy(SysResource::getParentId, Collectors.mapping(SysResource::getResourceName, Collectors.toSet())))
            .forEach((key,val)->{
                menuElRespList.add(MenuElVo.builder().elKey(key).elValue(val).build());
            });
        }
        return menuElRespList;
    }

    @Override
    public List<String> getServiceList(List<Long> userResourceIds) {
        Map<String,List<SysResource>> resourceMap = baseMapper.selectList(new QueryWrapper<SysResource>().lambda().in(SysResource::getId,userResourceIds)).stream().collect(Collectors.groupingBy(SysResource::getServiceModule));
        Iterator iterator = resourceMap.keySet().iterator();
        List<String> serviceList = Lists.newArrayList();
        while (iterator.hasNext()){
            serviceList.add(String.valueOf(iterator.next()));
        }
        return serviceList;
    }

    @Override
    public List<ServiceMenuResp> selectMenuTree() {
        List<SysResource> resourceList = baseMapper.selectList(new QueryWrapper<>());
        Map<String,List<SysResource>> groupResource = resourceList.stream().collect(Collectors.groupingBy(SysResource::getServiceModule));
        List<ServiceMenuResp> serviceMenuResps = Lists.newArrayList();
        Iterator key = groupResource.keySet().iterator();
        while (key.hasNext()){
            Object resourceKey = key.next();
            List<SysResource> resources = groupResource.get(resourceKey);
            //获取所有一级菜单
            List<SysResource> oneResources= resources.stream().filter(e -> e.getResourceType().equals(ConstantUtils.MENU_TYPE_0)).collect(Collectors.toList());
            List<TreeMenuVo> treeMenuVos = Lists.newArrayList();
            oneResources.forEach(resource ->{
                treeMenuVos.add(TreeMenuVo.builder()
                        .id(String.valueOf(resource.getId()))
                        .label(resource.getResourceName())
                        .sort(resource.getSort())
                        .children(getMenuChildren(resources, resource.getId()))
                        .build());
            });
            //升序排序
            Collections.sort(treeMenuVos, Comparator.comparing(TreeMenuVo::getSort));
            serviceMenuResps.add(ServiceMenuResp.builder()
                    .serviceCode(String.valueOf(resourceKey))
                    .treeMenuVo(treeMenuVos)
                    .build());
        }
        return serviceMenuResps;
    }

    /**
     * 递归获取菜单下级
     * @param resourceList
     * @param lastId
     * @return
     */
    public List<TreeMenuVo> getMenuChildren(List<SysResource> resourceList, Long lastId){
        List<TreeMenuVo> childrenList = new ArrayList<>();
        List<SysResource> nextResource= resourceList.stream().filter(e -> Objects.nonNull(e.getParentId()) && e.getParentId().longValue() == lastId.longValue()).collect(Collectors.toList());
        nextResource.forEach(menu ->{
            childrenList.add(TreeMenuVo.builder()
                    .id(String.valueOf(menu.getId()))
                    .label(menu.getResourceName())
                    .sort(menu.getSort())
                    .children(getMenuChildren(resourceList, menu.getId()))
                    .build());
        });
        //升序排序
        Collections.sort(childrenList, Comparator.comparing(TreeMenuVo::getSort));
        if (CollectionUtils.isEmpty(childrenList)) {
            return null;
        }
        return childrenList;
    }

    /**
     * 递归获取用户菜单下级
     * @param resourceList
     * @param lastId
     * @return
     */
    public List<UserTreeMenuVo> getUserMenuChildren(List<SysResource> resourceList, Long lastId){
        List<UserTreeMenuVo> childrenList = new ArrayList<>();
        //获取用户上级菜单的所有下级菜单 不包括元素
        List<SysResource> nextResource= resourceList.stream().filter(e -> Objects.nonNull(e.getParentId()) && e.getParentId().longValue() == lastId.longValue() && !Objects.equals(e.getResourceType(),ConstantUtils.MENU_TYPE_2) ).collect(Collectors.toList());
        nextResource.forEach(menu ->{
            childrenList.add(UserTreeMenuVo.builder()
                .path(menu.getResourceUrl())
                .component(menu.getComponent())
                .redirect(menu.getRedirect())
                .hideMenu(menu.getHideMenu())
                .sort(menu.getSort())
                .meta(MenuMetaVo.builder()
                    .id(menu.getId())
                    .rootId(menu.getRootId())
                    .parentId(menu.getParentId())
                    .resourceType(menu.getResourceType())
                    .title(menu.getResourceName())
                    .icon(menu.getIcon())
                    .isExternal(menu.getIsExternal())
                    .cache(menu.getCache())
                    .hideClose(menu.getHideClose())
                    .hideTabs(menu.getHideTabs())
                    .build())
                .children(getUserMenuChildren(resourceList,menu.getId()))
                .build());

        });
        //升序排序
        Collections.sort(childrenList, Comparator.comparing(UserTreeMenuVo::getSort));
        if (CollectionUtils.isEmpty(childrenList)) {
            return null;
        }
        return childrenList;
    }
}
