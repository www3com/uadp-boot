package com.upbos.cache.support;

import com.upbos.cache.SingleCache;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.concurrent.Callable;

/**
 * @author Jason
 */
public class AnnotationCache implements Cache {


    private final SingleCache singleCache;

    public AnnotationCache(SingleCache singleCache) {
        this.singleCache = singleCache;
    }

    @Override
    public String getName() {
        return this.singleCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object ret = singleCache.get(String.valueOf(key));
        if (ret == null) {
            return null;
        }
        return new SimpleValueWrapper(ret);
    }

    @Override
    public <T> T get(Object key, Class<T> aClass) {
        return singleCache.get(String.valueOf(key));
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        return singleCache.get(String.valueOf(key));
    }

    @Override
    public void put(Object key, Object value) {
        singleCache.set(String.valueOf(key), value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        return null;
    }

    @Override
    public void evict(Object key) {
        singleCache.remove(String.valueOf(key));
    }

    @Override
    public void clear() {

    }
}
