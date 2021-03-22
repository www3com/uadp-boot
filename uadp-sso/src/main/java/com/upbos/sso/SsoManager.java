/*******************************************************************************
 * @(#)SsoManager.java 2019年08月29日 17:11
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso;


import com.upbos.sso.cookie.CookieManager;
import com.upbos.sso.entity.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <b>Application name：</b> SsoManager.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.0.0 <br>
 */
public interface SsoManager {

    /**
     * 生成Token，并存入用户信息和用户数据
     *
     * @param request  request包装类
     * @param response response包装类
     * @param uid  用户id
     * @return token
     */
    Token login(HttpServletRequest request, HttpServletResponse response, String  uid);


    Token login(HttpServletRequest request, HttpServletResponse response, String  uid, Map<String, Object> data);

    /**
     * 生成指定类型的Token， 并存入用户数据
     * @param request request包装类
     * @param response response包装类
     * @param uid 用户id
     * @param tokenType token类型
     * @return token
     */
    Token login(HttpServletRequest request, HttpServletResponse response, String uid, String tokenType);

    Token login(HttpServletRequest request, HttpServletResponse response, String uid, Map<String, Object> data, String tokenType);

    /**
     * 销毁Token
     * @param request  request包装类
     * @param response response包装类
     */
    void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取Token
     *
     * @param tokenId
     * @return token
     */
    Token getToken(String tokenId);

    /**
     * 获取Token
     *
     * @param request request包装类
     * @return token
     */
    Token getToken(HttpServletRequest request);

    /**
     * 校验Token有效性
     *
     * @param request request包装类
     * @return token是否有效，true表示有效，false表示失效
     */
    boolean validateToken(HttpServletRequest request);

    boolean validateToken(String tokenId);

    /**
     * 存入用户数据
     *
     * @param request request包装类
     * @param key     数据key
     * @param value   数据值
     */
    void setAttr(HttpServletRequest request, String key, Object value);

    /**
     * 存入用户数据
     * @param tokenId tokenId
     * @param key 数据key
     * @param value 数据值
     */
    void setAttr(String tokenId, String key, Object value);
    /**
     * 获取数据
     *
     * @param request request包装类
     * @param key     数据key
     * @param <T>     数据值类型
     * @return 数据值
     */
    <T> T getAttr(HttpServletRequest request, String key);

    /**
     * 获取数据
     * @param tokenId tokenId
     * @param key     数据key
     * @param <T>     数据值类型
     * @return 数据值
     */
    <T> T getAttr(String tokenId, String key);
    /**
     * 销毁数据
     *
     * @param request request包装类
     * @param key     数据key
     */
    void removeAttr(HttpServletRequest request, String key);

    /**
     * 从session删除数据
     * @param tokenId 令牌Id
     * @param key 数据id
     */
    void removeAttr(String tokenId, String key);

    /**
     * 获取cookie管理器
     * @return
     */
    CookieManager getCookieManager();
}