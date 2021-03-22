package com.upbos.cache.support;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.upbos.cache.props.TimeUnit;
import com.upbos.cache.SingleCache;
import com.upbos.cache.props.CacheDetailProp;

import java.util.*;

/**
 * @author wangjz
 */
public class CaffeineCache implements SingleCache {
    private final Cache<String, Object> caffeineCache;

    private final String cacheName;

    public CaffeineCache(CacheDetailProp cacheProps) {
        Caffeine<Object, Object> cache = Caffeine.newBuilder();
        TimeUnit timeUnit = TimeUnit.parse(cacheProps.getExpireTime());
        if (CacheDetailProp.EXPIRE_POLICY_WRITE.equals(cacheProps.getExpirePolicy())) {
            cache.expireAfterWrite(timeUnit.getValue(), timeUnit.getUnit());
        } else {
            cache.expireAfterAccess(timeUnit.getValue(), timeUnit.getUnit());
        }
        cacheName = cacheProps.getCacheName();
        caffeineCache = cache.maximumSize(cacheProps.getMaximumSize()).build();
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) caffeineCache.getIfPresent(key);
    }

    @Override
    public void set(String key, Object value) {
        caffeineCache.put(key, value);
    }

    @Override
    public void remove(String key) {
        caffeineCache.invalidate(key);
    }

    @Override
    public <T> T getForHash(String key, String hashKey) {
        Map<String, T> map = this.get(key);
        return map == null ? null :  map.get(hashKey);
    }

    @Override
    public Set<String> keysForHash(String key) {
        Map<String, Object> map = this.get(key);
        return map == null ? null : map.keySet();
    }

    @Override
    public <K, V> Map<K, V> entriesForHash(String key) {
        return this.get(key);
    }

    @Override
    public List<Object> multiGetForHash(String key, Collection<String> hashKeys) {
        Map<String, Object> map =  this.get(key);

        List<Object> list = new ArrayList<>();
        if (map == null) {
            return list;
        }
        for (String hashKey : hashKeys) {
            Object item = map.get(hashKey);
            if (item != null) {
                list.add(item);
            }
        }
        return list;
    }


    @Override
    public void putForHash(String key, String hashKey, Object value) {
        Map<String, Object> valueMap = this.get(key);
        Map<String, Object> map = Optional.ofNullable(valueMap).orElseGet(() -> new HashMap<>(16));

        map.put(hashKey, value);
        this.set(key, map);
    }

    @Override
    public void putAllForHash(String key, Map<String, Object> value) {
        this.set(key, value);
    }

    @Override
    public void removeForHash(String key, String hashKey) {
        Map<String, Object> map =  this.get(key);
        if (map != null) {
            map.remove(hashKey);
            this.set(key, map);
        }
    }

    @Override
    public String getName() {
        return cacheName;
    }
}
