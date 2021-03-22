/*******************************************************************************
 * @(#)SsoStorageManager.java 2019年08月29日 17:11
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/

package com.upbos.sso.storage;


import com.upbos.sso.entity.Token;

import java.util.Map;

/**
 * <b>Application name：</b> SsoStorageManager.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.0.0 <br>
 */
public interface StorageManager {
    /**
     * 缓存token
     *
     * @param token 令牌
     */
    Token saveToken(String type, String uid, Map<String, Object> data);

    /**
     * 删除token
     *
     * @param tokenId 令牌id
     */
    void removeToken(String tokenId);

    /**
     * 获取token
     *
     * @param tokenId 令牌id
     * @return 令牌
     */
    Token getToken(String tokenId);


    /**
     * 保存用户自定义参数
     *
     * @param tokenId 令牌id
     * @param key     参数key
     * @param value   参数值
     */
    void setAttr(String tokenId, String key, Object value);

    /**
     * 删除自定义参数
     *
     * @param tokenId 令牌id
     * @param key     参数key
     */
    void removeAttr(String tokenId, String key);

    /**
     * 获取用户自定义参数
     *
     * @param tokenId 令牌id
     * @param key     参数key
     * @param <T>     参数值
     * @return
     */
    <T> T getAttr(String tokenId, String key);

}