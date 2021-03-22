/*******************************************************************************
 * @(#)WebSSOManager1.java 2018年09月24日 12:42
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso;

import cn.hutool.core.util.StrUtil;
import com.upbos.sso.SsoManager;
import com.upbos.sso.cookie.CookieManager;
import com.upbos.sso.entity.Token;
import com.upbos.sso.exception.SsoException;
import com.upbos.sso.exception.SsoExceptionCode;
import com.upbos.sso.storage.StorageManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <b>Application name：</b> WebSsoManager.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2018 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2018年09月24日 12:42 <br>
 * <b>@author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V1.0.0 <br>
 */

@Slf4j
public class SingleSsoManager implements SsoManager {


    private StorageManager storageManager;


    private CookieManager cookieManager;

    public void setCookieManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }


    @Override
    public Token login(HttpServletRequest request, HttpServletResponse response, String uid) {
        return this.login(request, response, uid, Token.TYPE_WEB);
    }

    @Override
    public Token login(HttpServletRequest request, HttpServletResponse response, String uid, Map<String, Object> data) {
        return this.login(request, response, uid, data, Token.TYPE_WEB);
    }


    @Override
    public Token login(HttpServletRequest request, HttpServletResponse response, String uid, String tokenType) {
        return this.login(request, response, uid, null, tokenType);
    }

    @Override
    public Token login(HttpServletRequest request, HttpServletResponse response, String uid, Map<String, Object> data, String tokenType) {
        if (tokenType == null || tokenType.indexOf(StrUtil.C_UNDERLINE) > -1) {
            throw new SsoException(SsoExceptionCode.TOKEN_ID_GENERATE_TYPE);
        }


        if (uid == null || uid.indexOf(StrUtil.C_UNDERLINE) > -1) {
            throw new SsoException(SsoExceptionCode.TOKEN_ID_GENERATE_UID);
        }

        Token token = storageManager.saveToken(tokenType, uid, data);
        cookieManager.setCookie(response, token.getId());

        return token;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String tokenId = cookieManager.getTokenId(request);
        if (tokenId == null) {
            return;
        }

        storageManager.removeToken(tokenId);
        cookieManager.removeCookie(response);
    }

    @Override
    public Token getToken(String tokenId) {
        return storageManager.getToken(tokenId);
    }

    @Override
    public Token getToken(HttpServletRequest request) {
        String tokenId = cookieManager.getTokenId(request);
        return tokenId == null ? null : storageManager.getToken(tokenId);
    }

    @Override
    public boolean validateToken(HttpServletRequest request) {
        String tokenId = getCookieManager().getTokenId(request);
        return validateToken(tokenId);
    }

    @Override
    public boolean validateToken(String tokenId) {
        Token token = getToken(tokenId);
        return token == null ? false : true;
    }

    @Override
    public void setAttr(HttpServletRequest request, String key, Object value) {
        Token token = getToken(request);
        if (token == null) {
            return;
        }
        storageManager.setAttr(token.getId(), key, value);
    }

    @Override
    public void setAttr(String tokenId, String key, Object value) {
        Token token = getToken(tokenId);
        if (token == null) {
            return;
        }
        storageManager.setAttr(token.getId(), key, value);
    }

    @Override
    public <T> T getAttr(HttpServletRequest request, String key) {
        Token token = getToken(request);
        return token == null ? null : storageManager.getAttr(token.getId(), key);
    }

    @Override
    public <T> T getAttr(String tokenId, String key) {
        return storageManager.getAttr(tokenId, key);
    }

    @Override
    public void removeAttr(HttpServletRequest request, String key) {
        Token token = getToken(request);
        if (token == null) {
            return;
        }
        storageManager.removeAttr(token.getId(), key);
    }

    @Override
    public void removeAttr(String tokenId, String key) {
        storageManager.removeAttr(tokenId, key);
    }

    @Override
    public CookieManager getCookieManager() {
        return this.cookieManager;
    }


}