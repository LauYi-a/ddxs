package com.ddx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ddx.auth.entity.SysAspectLog;
import com.ddx.basis.dto.vo.SysLogAspectVo;

/**
 * @ClassName: ISysAspectLogService
 * @Description: 切面日志 服务类
 * @author YI.LAU
 * @since 2022-04-29
 * @Version: 1.0
 */
public interface ISysAspectLogService extends IService<SysAspectLog> {

    /**
     * 存储日志
     * @param sysLogAspectVo
     * @return
     */
    Boolean addAspectLog(SysLogAspectVo sysLogAspectVo);

}
