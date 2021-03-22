/*******************************************************************************
 * @(#)SimpleCookieManager.java 2019年08月29日 17:11
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso.cookie;


import com.upbos.sso.entity.Token;
import com.upbos.sso.util.CookieUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <b>Application name：</b> SimpleCookieManager.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.0.0 <br>
 */
public class SimpleCookieManager implements CookieManager {


    private boolean httpOnly = false;

    private int maxAge = -1;

    private boolean rememberMe = false;

    private String tokenName = "sid";

    @Override
    public void setHttpOnly(Boolean httpOnly) {
        if (httpOnly != null) {
            this.httpOnly = httpOnly;
        }
    }

    @Override
    public void setMaxAge(Integer maxAge) {
        if (maxAge != null) {
            this.maxAge = maxAge;
        }
    }

    @Override
    public void setRememberMe(Boolean rememberMe) {
        if (rememberMe != null) {
            this.rememberMe = rememberMe;
        }

    }

    @Override
    public void setTokenName(String sid) {
        if (sid != null) {
            this.tokenName = sid;
        }
    }

    @Override
    public String getTokenName() {
        return this.tokenName;
    }


    @Override
    public void setCookie(HttpServletResponse response, String tokenId) {
        CookieUtils.setCookie(response, this.tokenName, tokenId, this.maxAge, this.httpOnly);
    }

    @Override
    public void removeCookie(HttpServletResponse response) {
        CookieUtils.removeCookie(response, this.tokenName);
    }

    @Override
    public String getTokenId(HttpServletRequest request) {
        String tokenId = CookieUtils.getCookieValue(request, this.tokenName);
        if (tokenId == null) {
            tokenId = request.getHeader(tokenName);
        }
        return tokenId;
    }

}
