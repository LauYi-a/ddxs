package com.ddx.sys.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.common.constant.CommonEnumConstant;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.utils.RedisTemplateUtils;
import com.ddx.sys.entity.SysWhitelistRequest;
import com.ddx.sys.mapper.SysWhitelistRequestMapper;
import com.ddx.sys.service.ISysWhitelistRequestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SysWhitelistRequestServiceImpl
 * @Description: 白名单路由 服务实现类
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Service
public class SysWhitelistRequestServiceImpl extends ServiceImpl<SysWhitelistRequestMapper, SysWhitelistRequest> implements ISysWhitelistRequestService {

    @Autowired
    private RedisTemplateUtils redisTemplate ;

    @Override
    public IPage<SysWhitelistRequest> selectPage(IPage<SysWhitelistRequest> arg0,  Wrapper<SysWhitelistRequest> arg1){
        return this.baseMapper.selectPage(arg0, arg1);
    }

    @Override
    public Boolean initWhitelistConfig() {
        try {
            List<String> requestWhitelist = baseMapper.selectList(new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getType, CommonEnumConstant.Dict.WHITELIST_TYPE_0.getDictKey()))
                    .stream().map(e -> { return e.getUrl(); }).collect(Collectors.toList());
            redisTemplate.del(ConstantUtils.WHITELIST_REQUEST);
            redisTemplate.set(ConstantUtils.WHITELIST_REQUEST, JSON.toJSONString(requestWhitelist));
            List<String> requestTimeWhitelist = baseMapper.selectList(new QueryWrapper<SysWhitelistRequest>().lambda().eq(SysWhitelistRequest::getType,CommonEnumConstant.Dict.WHITELIST_TYPE_1.getDictKey()))
                    .stream().map(e -> { return e.getUrl(); }).collect(Collectors.toList());
            redisTemplate.del(ConstantUtils.REQUEST_TIME_WHITELIST);
            redisTemplate.set(ConstantUtils.REQUEST_TIME_WHITELIST, JSON.toJSONString(requestTimeWhitelist));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
