package com.springboot.security.config.redis;

import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by umakr on 2018/4/28.
 * CacheContainer,这是一个辅助数据存储，将前面设置的缓存配置放入容器以便后面的逻辑获取。
 * 其中包含一个默认的缓存配置，防止 在未设置的情况导致缓存获取异常。
 */
public class CacheContainer {

    private static final String DEFAULT_CACHE_NAME="default";

    private static final Map<String,CacheItemConfig> CACHE_CONFIG_HOLDER=new ConcurrentHashMap(){
        {
            put(DEFAULT_CACHE_NAME,new CacheItemConfig(){
                @Override
                public String getName() {
                    return DEFAULT_CACHE_NAME;
                }

                @Override
                public long getExpiryTimeSecond() {
                    return 30;
                }

                @Override
                public long getPreLoadTimeSecond() {
                    return 25;
                }
            });
        }
    };

    public static void init(List<CacheItemConfig> cacheItemConfigs){
        if(CollectionUtils.isEmpty(cacheItemConfigs)){
            return;
        }
        cacheItemConfigs.forEach(cacheItemConfig -> {
            CACHE_CONFIG_HOLDER.put(cacheItemConfig.getName(),cacheItemConfig);
        });

    }

    public static CacheItemConfig getCacheItemConfigByCacheName(String cacheName){
        if(CACHE_CONFIG_HOLDER.containsKey(cacheName)) {
            return CACHE_CONFIG_HOLDER.get(cacheName);
        }
        return CACHE_CONFIG_HOLDER.get(DEFAULT_CACHE_NAME);
    }

    public static List<CacheItemConfig> getCacheItemConfigs(){
        return CACHE_CONFIG_HOLDER
                .values()
                .stream()
                .filter(new Predicate<CacheItemConfig>() {
                    @Override
                    public boolean test(CacheItemConfig cacheItemConfig) {
                        return !cacheItemConfig.getName().equals(DEFAULT_CACHE_NAME);
                    }
                })
                .collect(Collectors.toList());
    }
}
