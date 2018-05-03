package com.springboot.security.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisOperations;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义的redis缓存
 * Created by umakr on 2018/5/2.
 */
public class CustomizedRedisCache extends RedisCache{
    private static final Logger logger = LoggerFactory.getLogger(CustomizedRedisCache.class);

    private static  final Lock REFRESH_CACKE_LOCK = new ReentrantLock();
    private RedisOperations redisOperations;

    private CacheSupport getCacheSupport(){
        return ApplicationContextHelper.getApplicationContext().getBean(CacheSupport.class);
    }
    public CustomizedRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig,RedisOperations redisOperations) {
        super(name, cacheWriter, cacheConfig);
        this.redisOperations = redisOperations;
    }

//    getCache:当获取到缓存时，实时获取缓存的存活时间，如果存活时间进入缓存刷新时间范围即调起异步任务完成缓存动态加载。
// ThreadTaskHelper是一个异常任务提交的工具类。
// 下面方法中的参数key，并不是最终存入redis的key，是@Cacheable注解中的key，要想获取缓存的存活时间就需要找到真正的key，然后让redisOptions去调用ttl命令。
// 在springboot 1.5下面好像有个RedisCacheKey的对象，但在springboot2.0中并未发现，取而代之获取真正key是通过函数this.createCacheKey来完成。createCacheKey来完成
    public ValueWrapper get(final Object key){
        ValueWrapper valueWrapper = super.get(key);

        if(null!=valueWrapper){
            CacheItemConfig cacheItemConfig = CacheContainer.getCacheItemConfigByCacheName(key.toString());
            long preLoadTimeSecond=cacheItemConfig.getPreLoadTimeSecond();

            String cachekey = this.createCacheKey(key);
            Long ttl = this. redisOperations.getExpire(cachekey);

            if(null!=ttl && ttl<=preLoadTimeSecond){
                logger.info("key:{} ttl:{} preloadSecondTime:{}",cachekey,ttl,preLoadTimeSecond);

                if(ThreadTaskHelper.hasRuuningRefreshCacheTask(cachekey)){
                    logger.info("do not nees to refresh");
                }else{
                    ThreadTaskHelper.run(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                REFRESH_CACKE_LOCK.lock();
                                if(ThreadTaskHelper.hasRuuningRefreshCacheTask(cachekey)){
                                    logger.info("do not need to refresh");
                                }else{
                                    logger.info("refresh key:{}",cachekey);
//                                    System.out.println( CustomizedRedisCache.this.getCacheSupport());
                                    System.out.println( CustomizedRedisCache.super.getName());
                                    CustomizedRedisCache.this.getCacheSupport().refreshCacheByKey(CustomizedRedisCache.super.getName(), key.toString());
                                    ThreadTaskHelper.removeRefreshCacheTask(cachekey);
                                }
                            }finally {
                                REFRESH_CACKE_LOCK.unlock();
                            }
                        }
                    });
                }
            }
        }
        return  valueWrapper;
    }
}
