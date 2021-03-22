package com.upbos.cache.props;

import lombok.Data;

/**
 * @author wangjz
 */
@Data
public class CacheDetailProp {
    private String cacheName = DEFAULT_CACHE_NAME;
    private String expirePolicy = EXPIRE_POLICY_ACCESS;
    private String expireTime = "2h";
    private Integer maximumSize = 10000000;

    public static final String DEFAULT_CACHE_NAME = "cache";

    public static final String EXPIRE_POLICY_ACCESS = "expireAfterAccess";
    public static final String EXPIRE_POLICY_WRITE = "expireAfterWrite";
}
