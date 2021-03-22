/*******************************************************************************
 * @(#)CookieManager.java 2019年08月29日 17:11
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso.cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <b>Application name：</b> CookieManager.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.0.0 <br>
 */
public interface CookieManager {
	/**
	 * 设置httpOnly
	 * @param httpOnly true or false
	 */
    void setHttpOnly(Boolean httpOnly);

	/**
	 * 设置maxAge
	 * @param maxAge 最大存活时间
	 */
	void setMaxAge(Integer maxAge);

	/**
	 * 记住我
	 * @param rememberMe 是否记住
	 */
    void setRememberMe(Boolean rememberMe);

	/**
	 * 设置tokenId名称
	 * @param sid tokenId名称
	 */
	void setTokenName(String sid);

	/**
	 * 获取cookie管理器
	 * @return cookie管理器
	 */
	String getTokenName();
	/**
	 * 设置cookie
	 * @param response response包装类
	 * @param tokenId tokenId
	 */
    void setCookie(HttpServletResponse response, String tokenId);

	/**
	 * 删除cookie
	 * @param response response包装类
	 */
	void removeCookie(HttpServletResponse response);

	/**
	 * 获取token id
	 * @param request 包装类
	 * @return tokenId
	 */
    String getTokenId(HttpServletRequest request);
}