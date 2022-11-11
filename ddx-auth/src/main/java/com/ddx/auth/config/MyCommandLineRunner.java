package com.ddx.auth.config;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.auth.entity.SysRole;
import com.ddx.auth.entity.SysWhitelistRequest;
import com.ddx.auth.model.resp.SysRolePermissionResp;
import com.ddx.auth.service.ISysPermissionService;
import com.ddx.auth.service.ISysWhitelistRequestService;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.enums.CommonEnumConstant;
import com.ddx.basis.model.vo.SysParamConfigVo;
import com.ddx.common.utils.RedisTemplateUtils;
import lombok.extern.slf4j.Slf4j;
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
    private RedisTemplateUtils redisTemplate;
    @Resource
    private ISysPermissionService sysPermissionService;
    @Resource
    private ISysWhitelistRequestService sysWhitelistRequestService;

    @Override
    public void run(String... args){
        log.info("开始初始化数据...");
        //获取资源初始化权限
        Thread initRolePermission = new Thread(()-> {
            initRolePermission();
        });
        initRolePermission.start();

        //获取系统参数配置初始化参数配置
        Thread initSysParam = new Thread(()-> {
            initSysParam();
        });
        initSysParam.start();

        //初始化白名单
        Thread initWhite = new Thread(()-> {
            initWhite();
        });
        initWhite.start();
    }

    /**
     * 初始化权限
     */
    public void initRolePermission(){
        List<SysRolePermissionResp> list = sysPermissionService.listRolePermission();
        //先删除Redis中原来的资源hash表
        redisTemplate.del(ConstantUtils.OAUTH_URLS);
        list.parallelStream().peek(k -> {
            if (CollectionUtil.isNotEmpty(k.getRoles())) {
                List<Object> roles = new ArrayList<>();
                for (SysRole role : k.getRoles()) {
                    roles.add(ConstantUtils.ROLE_PREFIX + role.getCode());
                }
                //然后更新Redis中的资源
                redisTemplate.hset(ConstantUtils.OAUTH_URLS, k.getUrl(), roles);
            }
        }).collect(Collectors.toList());
        log.info("初始化角色权限完成...");
    }

    /**
     * 初始化系统参数
     */
    public void initSysParam(){
        if (redisTemplate.hasKey(ConstantUtils.SYS_PARAM_CONFIG)) {
            SysParamConfigVo sysParamConfigVo = (SysParamConfigVo) redisTemplate.get(ConstantUtils.SYS_PARAM_CONFIG);
            redisTemplate.set(ConstantUtils.SYS_PARAM_CONFIG, sysParamConfigVo);
        } else {
            redisTemplate.set(ConstantUtils.SYS_PARAM_CONFIG, SysParamConfigVo.builder()
                    .lpec(4)//登入错误次数默认四次
                    .accountLockTime(Long.valueOf(3600))//默认3600秒
                    .accessTokenTime(Long.valueOf(60 * 60 * 1))// 默认1小时
                    .refreshTokenTime(Long.valueOf(60 * 60 * 24 * 1)) //默认1天
                    .sysRequestTime(Long.valueOf(5))//系统请求时间默认5秒
                    .build());
        }
        log.info("初始化系统参数配置完成...");
    }

    /**
     * 初始化白名单
     */
    public void initWhite(){
        List<String> requestWhitelist = sysWhitelistRequestService.list(new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getType, CommonEnumConstant.Dict.WHITELIST_TYPE_0.getDictKey()))
                .stream().map(e -> { return e.getUrl(); }).collect(Collectors.toList());
        redisTemplate.del(ConstantUtils.WHITELIST_REQUEST);
        redisTemplate.set(ConstantUtils.WHITELIST_REQUEST, JSON.toJSONString(requestWhitelist));
        List<String> requestTimeWhitelist = sysWhitelistRequestService.list(new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getType,CommonEnumConstant.Dict.WHITELIST_TYPE_1.getDictKey()))
                .stream().map(e -> { return e.getUrl(); }).collect(Collectors.toList());
        redisTemplate.del(ConstantUtils.REQUEST_TIME_WHITELIST);
        redisTemplate.set(ConstantUtils.REQUEST_TIME_WHITELIST, JSON.toJSONString(requestTimeWhitelist));
        log.info("初始化系统参白名单完成...");
    }
}