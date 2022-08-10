package com.ddx.sys.service;

import com.ddx.sys.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

/**
 * @ClassName: ISysDictService
 * @Description:  字典服务类
 * @author YI.LAU
 * @since 2022-04-26
 * @Version: 1.0
 */
public interface ISysDictService extends IService<SysDict> {

    /**
     * 分页查询
     * @param arg0
     * @param arg1
     * @return
     */
    IPage<SysDict> selectPage(IPage<SysDict> arg0, Wrapper<SysDict> arg1);

    /**
     * 获取模块下所有字典组的字典键值
     * @return
     */
    Map<String,Object> getModulesAllGroupDictKeyValue();

}
