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

  /**
   * @param applicationContext
   * the applicationContext to set
   */
  public void setApplicationContext(ApplicationContext applicationContext) {
    ApplicationContextUtil.applicationContext = applicationContext;
  }

  public static <T> T getBean(Class<T> clazz) {
    if (applicationContext == null){
      return null;
    }
    return  applicationContext.getBean(clazz);
  }

  public static <T> T getBean(String beanName) {
    if (applicationContext == null){
      return null;
    }
    return (T) applicationContext.getBean(beanName);
  }

  /**
   * 通过name,以及Clazz返回指定的Bean
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    if (applicationContext == null){
      return null;
    }
    return getApplicationContext().getBean(name, clazz);
  }

  /**
   * @return the applicationContext
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

}