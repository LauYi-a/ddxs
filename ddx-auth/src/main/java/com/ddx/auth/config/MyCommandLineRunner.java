package com.ddx.auth.config;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.entity.SysPermission;
import com.ddx.auth.entity.SysRole;
import com.ddx.auth.entity.SysWhitelistRequest;
import com.ddx.auth.model.resp.SysRolePermissionResp;
import com.ddx.auth.service.ISysPermissionService;
import com.ddx.auth.service.ISysWhitelistRequestService;
import com.ddx.util.basis.constant.BasisConstant;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.redis.constant.RedisConstant;
import com.ddx.util.redis.template.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: MyCommandLineRunner
 * @Description: 在项目启动完成时从数据库将相关数据加载到Redis中
 * 单个线程异步执行业务模块
 * @Author: YI.LAU
 * @Date: 2022年03月24日
 * @Version: 1.0
 */
@Slf4j
@Component
@Order(value = 1) //指定其执行顺序,值越小优先级越高
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    private RedisTemplateUtil redisTemplate;
    @Resource
    private ISysPermissionService sysPermissionService;
    @Resource
    private ISysWhitelistRequestService sysWhitelistRequestService;

    @Override
    public void run(String... args){
        log.info("开始初始化数据...");
        //获取资源初始化角色权限
        Thread initRolePermission = new Thread(()-> {
            initRolePermission();
        });
        initRolePermission.start();

        //初始化权限资源
        Thread initPermission = new Thread(()->{
            initPermission();
        });
        initPermission.start();

        //初始化白名单
        Thread initWhite = new Thread(()-> {
            initWhite();
        });
        initWhite.start();
    }

    /**
     * 初始化权限
     */
    private void initRolePermission(){
        List<SysRolePermissionResp> list = sysPermissionService.listRolePermission();
        //先删除Redis中原来的资源hash表
        redisTemplate.del(RedisConstant.OAUTH_URLS);
        list.parallelStream().peek(k -> {
            if (CollectionUtil.isNotEmpty(k.getRoles())) {
                List<Object> roles = new ArrayList<>();
                for (SysRole role : k.getRoles()) {
                    roles.add(BasisConstant.ROLE_PREFIX + role.getCode());
                }
                //然后更新Redis中的资源
                redisTemplate.hset(RedisConstant.OAUTH_URLS, k.getUrl(), roles);
            }
        }).collect(Collectors.toList());
        log.info("初始化角色权限完成...");
    }

    /**
     * 初始化权限
     */
    private void initPermission(){
        List<SysPermission> permissions = sysPermissionService.list(new QueryWrapper<SysPermission>());
        //先删除Redis中原来的资源hash表
        redisTemplate.del(RedisConstant.PERMISSION_URLS);
        permissions.parallelStream().peek(k -> {
            if (StringUtils.isNoneBlank(k.getServiceModule())) {
                //然后更新Redis中的资源
                redisTemplate.hset(RedisConstant.PERMISSION_URLS,  k.getUrl().contains(BasisConstant.POST)?k.getUrl():BasisConstant.POST+k.getUrl(), k.getServiceModule());
            }
        }).collect(Collectors.toList());
        log.info("初始化权限完成...");
    }

    /**
     * 初始化白名单
     */
     private void initWhite(){
        List<String> requestWhitelist = sysWhitelistRequestService.list(new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getType, CommonEnumConstant.Dict.WHITELIST_TYPE_0.getDictKey()))
                .stream().map(e -> { return e.getUrl(); }).collect(Collectors.toList());
        redisTemplate.del(RedisConstant.WHITELIST_REQUEST);
        redisTemplate.set(RedisConstant.WHITELIST_REQUEST, JSON.toJSONString(requestWhitelist));
        List<String> requestTimeWhitelist = sysWhitelistRequestService.list(new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getType,CommonEnumConstant.Dict.WHITELIST_TYPE_1.getDictKey()))
                .stream().map(e -> { return e.getUrl(); }).collect(Collectors.toList());
        redisTemplate.del(RedisConstant.REQUEST_TIME_WHITELIST);
        redisTemplate.set(RedisConstant.REQUEST_TIME_WHITELIST, JSON.toJSONString(requestTimeWhitelist));
        log.info("初始化系统参白名单完成...");
    }
}