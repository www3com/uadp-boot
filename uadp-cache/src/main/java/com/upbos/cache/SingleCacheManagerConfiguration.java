package com.upbos.cache;


import com.upbos.cache.props.CacheProp;
import com.upbos.cache.support.AnnotationCacheManager;
import com.upbos.cache.support.CaffeineCacheManager;
import com.upbos.cache.support.RedisCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author wangjz
 */
@Configuration
@EnableConfigurationProperties(value = {CacheProp.class})
@ConditionalOnClass({SingleCacheManager.class})
public class SingleCacheManagerConfiguration {

    @Resource
    private CacheProp props;

    @Resource
    private RedisTemplate redisTemplate;

    @Bean("caffeineCacheManager")
    @ConditionalOnProperty(name = "cache.type", havingValue = "caffeine")
    public SingleCacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaches(props.getSpecs());
        return caffeineCacheManager;
    }

    @Bean("redisCacheManager")
    @ConditionalOnProperty(name = "cache.type", havingValue = "redis")
    public SingleCacheManager redisCacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisTemplate(redisTemplate);
        cacheManager.setSpecs(props.getSpecs());
        return cacheManager;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "cache.type")
    public CacheManager annotationCacheManager(SingleCacheManager singleCacheManager) {
        AnnotationCacheManager cacheManager = new AnnotationCacheManager();
        cacheManager.setCacheManager(singleCacheManager);
        return cacheManager;
    }
}