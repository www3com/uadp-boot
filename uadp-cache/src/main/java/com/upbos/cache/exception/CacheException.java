package com.upbos.cache.exception;


/**
 * @author wangjz
 */
public class CacheException extends RuntimeException {
    private static final long serialVersionUID = 6057602589533840889L;
    private int code;

    public CacheException(CacheExceptionCode cacheExceptionCode) {
        super(new StringBuffer("[").append(cacheExceptionCode.getCode()).append("]").append(cacheExceptionCode.getMsg()).toString());
        this.code = cacheExceptionCode.getCode();
    }

    public CacheException(Throwable throwable) {
        super(throwable);
    }

    public CacheException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public CacheException(int code, String msg) {
        super(new StringBuffer("[").append(code).append("]").append(msg).toString());
        this.code = code;
    }


    public int getStatus() {
        return this.code;
    }
}
