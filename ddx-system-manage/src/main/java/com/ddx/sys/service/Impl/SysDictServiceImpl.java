package com.ddx.sys.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddx.sys.entity.SysDict;
import com.ddx.sys.mapper.SysDictMapper;
import com.ddx.sys.service.ISysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: SysDictServiceImpl
 * @Description:  字典服务实现类
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    public IPage<SysDict> selectPage(IPage<SysDict> arg0,  Wrapper<SysDict> arg1){
        return this.baseMapper.selectPage(arg0, arg1);
    }

    @Override
    public Map<String,Object> getModulesAllGroupDictKeyValue() {
        Map<String,Map<String,List<SysDict>>> modulesGroupDict = baseMapper.selectList(new QueryWrapper<>()).stream().collect(Collectors.groupingBy(SysDict::getModules,Collectors.groupingBy(SysDict::getGroupType)));
        Iterator dictModulesKey = modulesGroupDict.keySet().iterator();
        Map<String,Object> dictModules = Maps.newHashMap();
        while (dictModulesKey.hasNext()){
            Map<String,Object> dictGroups = Maps.newHashMap();
            Object modulesKey = dictModulesKey.next();
            Map<String,List<SysDict>> groupDicts = modulesGroupDict.get(modulesKey);
            Iterator groupDictKey = groupDicts.keySet().iterator();
            while (groupDictKey.hasNext()){
                List<Map> dictKeyValues = new ArrayList<>();
                Object groupKey = groupDictKey.next();
                groupDicts.get(groupKey).forEach(e ->{
                    Map<String,Object> dictKeyValueMap = Maps.newHashMap();
                    dictKeyValueMap.put("key",e.getDictKey());
                    dictKeyValueMap.put("value",e.getDictValue());
                    dictKeyValueMap.put("desc",e.getDictDesc());
                    dictKeyValues.add(dictKeyValueMap);
                });
                dictGroups.put(String.valueOf(groupKey),dictKeyValues);
            }
            dictModules.put(String.valueOf(modulesKey),dictGroups);
        }
        return dictModules;
    }
}
