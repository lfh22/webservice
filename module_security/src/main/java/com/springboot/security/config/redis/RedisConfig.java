package com.springboot.security.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by umakr on 2018/4/28.
 */
@Configuration
@EnableCaching
public class RedisConfig  extends CachingConfigurerSupport{

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(){
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        return  factory;
//    }
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory){
        //创建一个模板
        RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();
        //将刚才的redis链接工厂设置到模板类中
        template.setConnectionFactory(factory);
        //设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化器
        //使用Jackson 2，将对象序列化为JSON
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //json转对象类，不设置默认的会将json转成hashmap
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public  CacheManager cacheManager(RedisConnectionFactory connectionFactory,RedisTemplate<Object,Object> redisTemplate){
        CacheItemConfig productCacheItemConfig=new CacheItemConfig("Product",30,25);
        List<CacheItemConfig> cacheItemConfigs= Lists.newArrayList(productCacheItemConfig);

        CustomizedRedisCacheManager cacheManager = new CustomizedRedisCacheManager(connectionFactory,redisTemplate,cacheItemConfigs);

        return cacheManager;
    }

//    @Bean
//    public RedisCacheManager cacheManager(RedisTemplate redisTemplate){
//        return new RedisCacheManager(redisTemplate);
//    }
}
