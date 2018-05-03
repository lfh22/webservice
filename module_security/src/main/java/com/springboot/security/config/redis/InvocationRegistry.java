package com.springboot.security.config.redis;

/**
 * Created by umakr on 2018/5/3.
 */

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 缓存方法注册接口
 */
public interface InvocationRegistry {

    void registerInvocation(Object invokedBean, Method invokedMethod, Object[] invocationArguments, Set<String> cacheNames);

}
