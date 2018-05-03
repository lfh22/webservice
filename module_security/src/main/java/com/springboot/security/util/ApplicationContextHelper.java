package com.springboot.security.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by umakr on 2018/5/2.
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware{
    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
//        synchronized (ApplicationContextHelper.class) {
//            ApplicationContextHelper.ctx = applicationContext;
//            ApplicationContextHelper.class.notifyAll();
//        }


    }

    public ApplicationContextHelper(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
    public static ApplicationContext getApplicationContext(){
        return ctx;
    }
    public static Object getBean(String beanName) {
        return ctx.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        T t = null;
        Map<String, T> map = ctx.getBeansOfType(clazz);
        for (Map.Entry<String, T> entry : map.entrySet()) {
            t = entry.getValue();
        }
        return t;
    }
}
