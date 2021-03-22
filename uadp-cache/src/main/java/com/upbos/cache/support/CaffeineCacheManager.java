package com.upbos.cache.support;

import com.upbos.cache.exception.CacheException;
import com.upbos.cache.exception.CacheExceptionCode;
import com.upbos.cache.SingleCache;
import com.upbos.cache.SingleCacheManager;
import com.upbos.cache.props.CacheDetailProp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangjz
 */
public class CaffeineCacheManager implements SingleCacheManager {
    private Map<String, SingleCache> cacheMap ;

    private List<CacheDetailProp> cachePropsList;

    public void setCaches(List<CacheDetailProp> caches) {
        this.cachePropsList = caches;
        afterPropertiesSet();
    }

    public void setCache(CacheDetailProp cache) {
        this.setCaches(Arrays.asList(cache));
    }

    private void afterPropertiesSet() {
        if (cacheMap == null) {
            cacheMap = new ConcurrentHashMap<String, SingleCache>(16);
            for (CacheDetailProp cacheProps : cachePropsList) {
                SingleCache singleCache = new CaffeineCache(cacheProps);
                cacheMap.put(cacheProps.getCacheName(), singleCache);
            }
        }
    }
    @Override
    public SingleCache getCache(String cacheName) {
        SingleCache singleCache = cacheMap.get(cacheName);
        if (singleCache == null) {
           throw  new CacheException(CacheExceptionCode.CACHE_NAME_NULL.getCode(), "找不到缓存：" + cacheName);
        }else {
            return singleCache;
        }
    }

    @Override
    public Map<String, SingleCache> getCaches() {
        return cacheMap;
    }


}
