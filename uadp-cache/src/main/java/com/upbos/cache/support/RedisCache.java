package com.upbos.cache.support;

import com.upbos.cache.props.TimeUnit;
import com.upbos.cache.props.CacheDetailProp;
import com.upbos.cache.SingleCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangjz
 */
@SuppressWarnings("unchecked")
public class RedisCache implements SingleCache {

    private CacheDetailProp cacheDetailProp;

    private RedisTemplate redisTemplate;

    public RedisCache(CacheDetailProp cacheDetailProp, RedisTemplate redisTemplate) {
        this.cacheDetailProp = cacheDetailProp;
        this.redisTemplate = redisTemplate;
    }

    private String getRedisKey(String key) {
        return new StringBuffer(cacheDetailProp.getCacheName()).append(":").append(key).toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object ret = redisTemplate.opsForValue().get(getRedisKey(key));
        this.expire(key, false);
        return (T) ret;
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(getRedisKey(key), value);
        this.expire(key, true);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(getRedisKey(key));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getForHash(String key, String hashKey) {
        this.expire(key, false);
        return (T) redisTemplate.opsForHash().get(getRedisKey(key), hashKey);
    }

    @Override
    public Set<String> keysForHash(String key) {
        return redisTemplate.opsForHash().keys(getRedisKey(key));
    }

    @Override
    public <K, V> Map<K, V> entriesForHash(String key) {
        this.expire(key, false);
        return redisTemplate.opsForHash().entries(getRedisKey(key));
    }

    @Override
    public List<Object> multiGetForHash(String key, Collection<String> hashKeys) {
        return redisTemplate.opsForHash().multiGet(getRedisKey(key), hashKeys);
    }

    @Override
    public void putForHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(getRedisKey(key), hashKey, value);
        this.expire(key, true);
    }

    @Override
    public void putAllForHash(String key, Map<String, Object> value) {
        redisTemplate.opsForHash().putAll(getRedisKey(key), value);
        this.expire(key, true);
    }

    @Override
    public void removeForHash(String key, String hashKey) {
        redisTemplate.opsForHash().delete(getRedisKey(key), hashKey);
    }

    @Override
    public String getName() {
        return cacheDetailProp.getCacheName();
    }

    @SuppressWarnings("unchecked")
    private void expire(String key, boolean isSet) {
        // 如果有配置，则进行过期设置
        TimeUnit timeUnit = TimeUnit.parse(cacheDetailProp.getExpireTime());
        if (isSet) {
            redisTemplate.expire(getRedisKey(key), timeUnit.getValue(), timeUnit.getUnit());
        } else if (cacheDetailProp.getExpirePolicy().equals(CacheDetailProp.EXPIRE_POLICY_ACCESS)) {
            redisTemplate.expire(getRedisKey(key), timeUnit.getValue(), timeUnit.getUnit());
        }
    }
}
