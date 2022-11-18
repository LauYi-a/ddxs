package com.ddx.sys.service.Impl;

import com.ddx.sys.service.ISysDictService;
import com.ddx.util.basis.constant.CommonEnumConstant;
import com.ddx.util.basis.model.vo.DictVo;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

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
public class SysDictServiceImpl  implements ISysDictService {

    @Override
    public Map<String,Object> getModulesAllGroupDictKeyValue() {
        Map<String,Map<String,List<DictVo>>> modulesGroupDict = CommonEnumConstant.Dict.getDictList().stream().collect(Collectors.groupingBy(DictVo::getModules,Collectors.groupingBy(DictVo::getGroupType)));
        Iterator dictModulesKey = modulesGroupDict.keySet().iterator();
        Map<String,Object> dictModules = Maps.newHashMap();
        while (dictModulesKey.hasNext()){
            Map<String,Object> dictGroups = Maps.newHashMap();
            Object modulesKey = dictModulesKey.next();
            Map<String,List<DictVo>> groupDicts = modulesGroupDict.get(modulesKey);
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
