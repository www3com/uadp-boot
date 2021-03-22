package com.upbos.cache.exception;

/**
 * @author wangjz
 */

public enum CacheExceptionCode {
    /**
     * 找不到缓存
     */
    CACHE_NAME_NULL(201, "找不到缓存，请检查配置文件中的缓存配置");


    private int code;
    private String msg;

    CacheExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
