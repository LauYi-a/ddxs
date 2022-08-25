package com.ddx.sys.service;

import java.util.Map;

/**
 * @ClassName: ISysDictService
 * @Description:  字典服务类
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
public interface ISysDictService {

    /**
     * 获取模块下所有字典组的字典键值
     * @return
     */
    Map<String,Object> getModulesAllGroupDictKeyValue();

}
