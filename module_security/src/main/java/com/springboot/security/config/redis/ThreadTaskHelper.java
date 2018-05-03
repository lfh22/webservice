package com.springboot.security.config.redis;


import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by umakr on 2018/5/2.
 */
public class ThreadTaskHelper {

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    private static Map RUNNING_REFRESH_CACHE = Maps.newConcurrentMap();

    public static Map<String,String> getRunningRefreshCache(){
        return RUNNING_REFRESH_CACHE;
    }

    public static void putRefreshCacheTask(String cacheKey){
        if(!hasRuuningRefreshCacheTask(cacheKey)){
            RUNNING_REFRESH_CACHE.put(cacheKey,cacheKey);
        }
    }

    public static void removeRefreshCacheTask(String cacheKay){
        if(hasRuuningRefreshCacheTask(cacheKay)){
            RUNNING_REFRESH_CACHE.remove(cacheKay);
        }
    }

    public static boolean hasRuuningRefreshCacheTask(String cacheKey) {
        return RUNNING_REFRESH_CACHE.containsKey(cacheKey);
    }

    public static void run(Runnable runnable){
        executorService.execute(runnable);
    }
}
