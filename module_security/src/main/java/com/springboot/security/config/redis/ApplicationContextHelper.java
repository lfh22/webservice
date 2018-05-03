package com.springboot.security.config.redis;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by umakr on 2018/5/2.
 */
public class ApplicationContextHelper implements ApplicationContextAware{
    private static ApplicationContext ctx;

    @Override
    synchronized public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return ctx;
    }
}
