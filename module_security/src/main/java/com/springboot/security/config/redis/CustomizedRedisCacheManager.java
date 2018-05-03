package com.springboot.security.config.redis;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by umakr on 2018/4/28.
 */
public class CustomizedRedisCacheManager extends RedisCacheManager {


    private RedisCacheWriter redisCacheWriter;
    private RedisCacheConfiguration defaultRedisCacheConfiguration;
    private RedisOperations redisOperations;

    /**
     * connectionFactory，这是一个redis连接工厂，用于后续操作redis
     * redisOperations,这个一个redis的操作实例，具体负责执行redis命令
     * cacheItemConfigList,这是缓存的配置，比如名称，失效时间，主动刷新时间，用于取代在注解上个性化的配置。
     * 核心思路就是调用RedisCacheManager的构造函数。
     */
    public CustomizedRedisCacheManager(
            RedisConnectionFactory connectionFactory,
            RedisOperations redisOperations,
            List<CacheItemConfig> cacheItemConfigList) {

        this(
                RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(30)),
                cacheItemConfigList
                        .stream()
                        .collect(Collectors.toMap(CacheItemConfig::getName, cacheItemConfig -> {
                            RedisCacheConfiguration cacheConfiguration =
                                    RedisCacheConfiguration
                                            .defaultCacheConfig()
                                            .entryTtl(Duration.ofSeconds(cacheItemConfig.getExpiryTimeSecond()))
                                            .prefixKeysWith(cacheItemConfig.getName());
                            return cacheConfiguration;
                        }))
        );
        this.redisOperations=redisOperations;
        CacheContainer.init(cacheItemConfigList);

    }
    public CustomizedRedisCacheManager(
            RedisCacheWriter redisCacheWriter
            ,RedisCacheConfiguration redisCacheConfiguration,
            Map<String, RedisCacheConfiguration> redisCacheConfigurationMap) {
        super(redisCacheWriter,redisCacheConfiguration,redisCacheConfigurationMap);
        this.redisCacheWriter=redisCacheWriter;
        this.defaultRedisCacheConfiguration=redisCacheConfiguration;
    }
//    由于我们需要主动刷新缓存，所以需要重写getCache方法：主要就是将RedisCache构造函数所需要的参数传递过去。
    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);

        if(null == cache){
            return  cache;
        }
        CustomizedRedisCache redisCache = new CustomizedRedisCache(
                name,
                this.redisCacheWriter,
                this.defaultRedisCacheConfiguration,
                this.redisOperations
        );
        return redisCache;
    }
}
