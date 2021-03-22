package com.upbos.data.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service("dataSpringContext")
public class SpringContext implements ApplicationContextAware {
	/**
	 * SpringContext 上下文环境
	 */
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContext.applicationContext = applicationContext;
	}

	public static <T> T getBean(Class cls) throws BeansException {
		return (T)applicationContext.getBean(cls);
	}
}
