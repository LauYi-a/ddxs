package com.ddx.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ddx.common.constant.ConstantUtils;
import com.ddx.common.utils.OauthUtils;
import com.ddx.common.utils.RequestContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * @ClassName: MyMetaObjectHandler
 * @Description: mybatis 自动填充器
 * @Author: YI.LAU
 * @Date: 2022年04月08日
 * @Version: 1.0
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("start insert fill...");
		this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
		this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		this.setFieldValByName("createId", Long.valueOf(OauthUtils.getCurrentUser().getUserId()), metaObject);
		this.setFieldValByName("createName",OauthUtils.getCurrentUser().getNickname(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("start update fill...");
		this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		this.setFieldValByName("updateId",Long.valueOf(OauthUtils.getCurrentUser().getUserId()), metaObject);
		this.setFieldValByName("updateName",OauthUtils.getCurrentUser().getNickname(), metaObject);

	}
}