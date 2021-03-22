/*******************************************************************************
 * @(#)CrossDomainClientInterceptor.java 2019年08月29日 17:11
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/

package com.upbos.sso.interceptor.client;


import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.upbos.sso.SsoManager;
import com.upbos.sso.constant.Constants;
import com.upbos.sso.entity.ChainRet;
import com.upbos.sso.exception.SsoException;
import com.upbos.sso.exception.SsoExceptionCode;
import com.upbos.sso.interceptor.Interceptor;
import com.upbos.sso.util.CookieUtils;
import com.upbos.sso.util.ServletUtils;
import com.upbos.sso.util.StringPool;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * <b>Application name：</b> CrossDomainClientInterceptor.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.0.0 <br>
 */
@Slf4j
public class SsoClientInterceptor implements Interceptor {


    /**
     * 跨域登录验证服务器地址
     */
    private String ssoServerUrl;

    private String tokenName;

    public void setSsoServerUrl(String ssoServerUrl) {
        this.ssoServerUrl = ssoServerUrl;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    public ChainRet preHandle(HttpServletRequest request, HttpServletResponse response, SsoManager ssoManager) {
        if (this.ssoServerUrl == null) {
            throw new SsoException(SsoExceptionCode.INTERCEPTOR_SSO_SERVER_URL_NULL);
        }

        String tokenId = request.getParameter(Constants.SESSION_KEY_TOKEN);
        if (ssoManager.validateToken(request)) {
            if (tokenId != null) {
                filterTokenParameter(request, response);
            }
            return ChainRet.NEXT;
        }

        //如果请求地址带有参数_token，则验证token的有效性，并过滤掉_token参数
        if (tokenId == null) {
            StringBuffer redirectUrl = new StringBuffer(ssoServerUrl)
                    .append(StringPool.QUESTION_MARK).append(Constants.RETURN_URL_KEY).append(StringPool.EQUALS)
                    .append(URLUtil.encode(request.getRequestURL().toString()));
            ServletUtils.sendRedirect(response, redirectUrl.toString());
            log.debug("当前请求未认证，重定向单点登录服务器：{},当前请求url：{}", ssoServerUrl, request.getRequestURL());
        } else {
            CookieUtils.setCookie(response, this.tokenName, tokenId);
            filterTokenParameter(request, response);
        }
        return ChainRet.BREAK;
    }

    /**
     * 重定向请求页面，过滤掉参数:_token
     *
     * @param request
     * @param response
     */
    private void filterTokenParameter(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getRequestURL().toString();
        Map<String, String> params = ServletUtil.getParamMap(request);
        StringBuffer paramsBuffer = new StringBuffer();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (!key.equals(Constants.RETURN_URL_KEY)) {
                paramsBuffer.append(StringPool.AMPERSAND).append(key).append(StringPool.EQUALS).append(params.get(key));
            }
        }
        ServletUtils.sendRedirect(response, url + (paramsBuffer.length() > 0 ? StringPool.QUESTION_MARK + paramsBuffer.substring(1) : StringPool.EMPTY));
    }

    @Override
    public int getPriority() {
        return -999;
    }

    @Override
    public String getName() {
        return "sso-client";
    }
}
