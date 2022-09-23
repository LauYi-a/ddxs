package com.ddx.sys.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddx.basis.constant.ConstantUtils;
import com.ddx.basis.dto.vo.SysLogAspectVo;
import com.ddx.basis.utils.ThreadPoolUtils;
import com.ddx.sys.entity.SysAspectLog;
import com.ddx.sys.mapper.SysAspectLogMapper;
import com.ddx.sys.service.ISysAspectLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SysAspectLogServiceImpl
 * @Description: 切面日志 服务实现类
 * @author YI.LAU
 * @since 2022-04-29
 * @Version: 1.0
 */
@Slf4j
@Service
public class SysAspectLogServiceImpl extends ServiceImpl<SysAspectLogMapper, SysAspectLog> implements ISysAspectLogService {

    @Override
    public IPage<SysAspectLog> selectPage(IPage<SysAspectLog> arg0,  Wrapper<SysAspectLog> arg1){
        return this.baseMapper.selectPage(arg0, arg1);
    }

    @Override
    public Boolean addAspectLog(SysLogAspectVo sysLogAspectVo) {
        try {
            ThreadPoolUtils.execute(ConstantUtils.THREAD_POOL_LOG_NAME,() -> {
                SysAspectLog sysAspectLog = new SysAspectLog();
                BeanUtils.copyProperties(sysLogAspectVo,sysAspectLog);
                sysAspectLog.setHeader(JSON.toJSONString(sysLogAspectVo.getHeader()));
                sysAspectLog.setParam(JSON.toJSONString(sysLogAspectVo.getData()));
                baseMapper.insert(sysAspectLog);
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }
}
