package com.upbos.cache;

import java.util.Map;

/**
 * @author wangjz
 */
public interface SingleCacheManager {
    /**
     * 根据缓存名称获取缓存
     * @param cacheName
     * @return
     */
    SingleCache getCache(String cacheName);

    /**
     * 获取所有缓存
     * @return 所有缓存
     */
    Map<String, SingleCache> getCaches();
}
