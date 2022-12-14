package com.ddx.web.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
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
		this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
		this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		this.setFieldValByName("createId", UserOauthInfo.getCurrentUser().getUserId(), metaObject);
		this.setFieldValByName("updateId", UserOauthInfo.getCurrentUser().getUserId(), metaObject);
		this.setFieldValByName("createName", UserOauthInfo.getCurrentUser().getNickname(), metaObject);
		this.setFieldValByName("updateName", UserOauthInfo.getCurrentUser().getNickname(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		this.setFieldValByName("updateId",UserOauthInfo.getCurrentUser().getUserId(), metaObject);
		this.setFieldValByName("updateName", UserOauthInfo.getCurrentUser().getNickname(), metaObject);

	}
}