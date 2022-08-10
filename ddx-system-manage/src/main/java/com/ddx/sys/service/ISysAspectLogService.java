package com.ddx.sys.service;

import com.ddx.common.dto.vo.SysLogAspectVo;
import com.ddx.sys.entity.SysAspectLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @ClassName: ISysAspectLogService
 * @Description: 切面日志 服务类
 * @author YI.LAU
 * @since 2022-04-29
 * @Version: 1.0
 */
public interface ISysAspectLogService extends IService<SysAspectLog> {

    /**
     * 分页查询
     * @param arg0
     * @param arg1
     * @return
     */
    IPage<SysAspectLog> selectPage(IPage<SysAspectLog> arg0, Wrapper<SysAspectLog> arg1);

    /**
     * 存储日志
     * @param sysLogAspectVo
     * @return
     */
    Boolean addAspectLog(SysLogAspectVo sysLogAspectVo);

}
