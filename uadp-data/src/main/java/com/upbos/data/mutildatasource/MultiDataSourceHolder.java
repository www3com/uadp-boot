/*******************************************************************************
 * @(#)MultiDataSourceContextHolder.java 2018年10月03日 10:45
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.mutildatasource;

import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Title: MultiDataSourceHolder.java</p>
 * <p>Description: 线程事务绑定</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class MultiDataSourceHolder {
    /**
     * 用于在切换数据源时保证不会被其他线程修改
     */
    private static Lock lock = new ReentrantLock();

    /**
     * Maintain variable for every thread, to avoid effect other thread
     */
    private static final ThreadLocal<Stack> CONTEXT_HOLDER = new ThreadLocal<Stack>();

    /**
     * 保存线程数据源
     * @param dataSourceKey
     */
    public static void setKey(String dataSourceKey) {
        lock.lock();
        Stack dsStack = CONTEXT_HOLDER.get();
        if (dsStack == null) {
            dsStack=new Stack();
        }
        dsStack.push(dataSourceKey);
        CONTEXT_HOLDER.set(dsStack);
        lock.unlock();
    }

    /**
     * 获取当前数据源key
     * @return
     */
    public static String getKey() {
        Stack dsStack = CONTEXT_HOLDER.get();
        if (dsStack != null && !dsStack.isEmpty()) {
            return (String) dsStack.peek();
        }
        return null;
    }

    /**
     * 清除数据源
     */
    public static void clearKey() {
        lock.lock();
        Stack dsStack = CONTEXT_HOLDER.get();
        if (dsStack != null && !dsStack.isEmpty()) {
            dsStack.pop();
        }
        lock.unlock();
    }
}