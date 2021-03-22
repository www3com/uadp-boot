package com.upbos.cache.support;

import com.upbos.cache.SingleCache;
import com.upbos.cache.SingleCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangjz
 */
public class AnnotationCacheManager implements CacheManager {

    private Map<String, AnnotationCache> cacheMap = new ConcurrentHashMap<String, AnnotationCache>();

    public void setCacheManager(SingleCacheManager singleCacheManager) {
        Map<String, SingleCache> singleCaches = singleCacheManager.getCaches();
        for (String cacheName : singleCaches.keySet()) {
            AnnotationCache annotationCache = new AnnotationCache(singleCaches.get(cacheName));
            cacheMap.put(cacheName, annotationCache);
        }
    }

    @Override
    public Cache getCache(String cacheName) {
        return cacheMap.get(cacheName);
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }
}
