package com.upbos.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangjz
 */
public interface SingleCache {
    /**
     * 获取缓存对象
     *
     * @param key 缓存key
     * @return 缓存对象
     */
    <T> T get(String key);

    /**
     * 加入缓存
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    void set(String key, Object value);

    /**
     * 删除缓存对象
     *
     * @param key 缓存key
     */
    void remove(String key);

    /**
     * 获取hash缓存对象
     *
     * @param key 缓存key
     * @param hashKey hash缓存key
     * @return 实体对象
     */
    <T> T getForHash(String key, String hashKey);

    /**
     * 遍历hash缓存的所有key
     * @param key 缓存key
     * @return 队列
     */
    Set<String> keysForHash(String key);

    /**
     * 获取hash缓存对象
     *
     * @param key 缓存key
     * @return 缓存对象
     */
    <K, V> Map<K, V> entriesForHash(String key);

    /**
     * 获取缓存对象
     * @param key 缓存key
     * @param hashKeys hash key
     * @return
     */
     List<Object> multiGetForHash(String key, Collection<String> hashKeys);

    /**
     * 加入hash缓存
     *
     * @param key     缓存key
     * @param hashKey hashKey
     * @param value   缓存value
     */
    void putForHash(String key, String hashKey, Object value);

    /**
     * 加入hash缓存value
     *
     * @param key   缓存key
     * @param value 缓存对象
     */
    void putAllForHash(String key, Map<String, Object> value);

    /**
     * 删除hash缓存对象
     *
     * @param key     缓存key
     * @param hashKey 缓存hashKey
     */
    void removeForHash(String key, String hashKey);

    /**
     * 获取缓存名称
     * @return 缓存名称
     */
    String getName();
}
