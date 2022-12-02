package com.ddx.web.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ApplicationContextUtil
 * @Description: 手动注入bean
 * @Author: YI.LAU
 * @Date: 2022年11月03日 14:42
 * @Version: 1.0
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

  protected static ApplicationContext applicationContext = null;

  public static <T> T getBean(String beanName) {
    return (T) applicationContext.getBean(beanName);
  }

  public static <T> T getBean(Class<T> name) {
    return  applicationContext.getBean(name);
  }

  /**
   * @param applicationContext
   *            the applicationContext to set
   */
  public void setApplicationContext(ApplicationContext applicationContext) {
    ApplicationContextUtil.applicationContext = applicationContext;
  }

  /**
   * @return the applicationContext
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 通过name,以及Clazz返回指定的Bean
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }
}