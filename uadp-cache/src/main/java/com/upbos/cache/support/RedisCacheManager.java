package com.upbos.cache.support;

import com.upbos.cache.exception.CacheException;
import com.upbos.cache.exception.CacheExceptionCode;
import com.upbos.cache.SingleCache;
import com.upbos.cache.SingleCacheManager;
import com.upbos.cache.props.CacheDetailProp;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangjz
 */
public class RedisCacheManager implements SingleCacheManager {


    private Map<String, SingleCache> caches;


    private RedisTemplate redisTemplate;

    public RedisCacheManager() {
        super();
    }

    public RedisCacheManager(RedisTemplate redisTemplate, List<CacheDetailProp> cacheDetailProps) {
        this.setRedisTemplate(redisTemplate);
        this.setSpecs(cacheDetailProps);
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        this.redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
//        this.redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
    }

    public void setSpecs(List<CacheDetailProp> cacheDetailProps) {
        caches = new ConcurrentHashMap<String, SingleCache>(16);
        for(CacheDetailProp cacheDetailProp : cacheDetailProps) {
            SingleCache singleCache = new RedisCache(cacheDetailProp, redisTemplate);
            caches.put(cacheDetailProp.getCacheName(), singleCache);
        }
    }

    @Override
    public SingleCache getCache(String cacheName) {
        return Optional.ofNullable(caches.get(cacheName)).orElseThrow(() ->
                new CacheException(CacheExceptionCode.CACHE_NAME_NULL.getCode(), "找不到缓存：" + cacheName));
    }

    @Override
    public Map<String, SingleCache> getCaches() {
        return caches;
    }
}
