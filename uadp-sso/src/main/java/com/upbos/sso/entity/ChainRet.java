package com.upbos.sso.entity;

/**
 * 拦截器链状态
 * @author wangjz
 */
public enum ChainRet {
    /**
     * 执行下一个拦截器
     */
    NEXT,
    /**
     * 跳过后面所有拦截器
     */
    SKIP,
    /**
     * 中断拦截器链
     */
    BREAK;
}
