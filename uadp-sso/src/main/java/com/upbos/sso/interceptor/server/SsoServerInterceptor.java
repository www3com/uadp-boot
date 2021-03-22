/*******************************************************************************
 * @(#)CrossDomainServerInterceptor.java 2019年08月29日 17:11
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso.interceptor.server;


import cn.hutool.core.util.URLUtil;
import com.upbos.sso.SsoManager;
import com.upbos.sso.constant.Constants;
import com.upbos.sso.entity.ChainRet;
import com.upbos.sso.entity.Token;
import com.upbos.sso.interceptor.Interceptor;
import com.upbos.sso.util.ServletUtils;
import com.upbos.sso.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <b>Application name：</b> CrossDomainServerInterceptor.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.0.0 <br>
 */
@Slf4j
public class SsoServerInterceptor implements Interceptor {

    private String ssoServerUrl = "/**/sso/sign.do";

    private String ssoLoginUrl = "";

    public void setSsoServerUrl(String ssoServerUrl) {
        this.ssoServerUrl = ssoServerUrl;
    }

    public void setSsoLoginUrl(String ssoLoginUrl) {
        this.ssoLoginUrl = ssoLoginUrl;
    }

    @Override
    public ChainRet preHandle(HttpServletRequest request, HttpServletResponse response, SsoManager ssoManager) {

        //如果请求URL不是获取token，则不进入此拦截器工作
        String requestUri = request.getRequestURI();
        PathMatcher matcher = new AntPathMatcher();

        // 放行会话过期页面expireUrl
        if (!matcher.match(this.ssoServerUrl, requestUri)) {
            return ChainRet.NEXT;
        }

        String returnUrl = request.getParameter(Constants.RETURN_URL_KEY);

        if (returnUrl == null) {
            ServletUtils.write(response, "缺少参数returnUrl");
            log.info("sso客户端请求地址缺少参数returnUrl");
            return ChainRet.BREAK;
        }

        Token token = ssoManager.getToken(request);

        if (token == null) {
            // 如果服务端也没有登录，则跳转到登录界面
            redirectUrl(response, this.ssoLoginUrl, Constants.RETURN_URL_KEY, returnUrl);
            log.debug("SSO服务没有获取到token，重定向登录页面：{}，回调地址：{}", this.ssoLoginUrl, returnUrl);
        } else {
            returnUrl = URLUtil.decode(returnUrl);
            redirectUrl(response, returnUrl, Constants.SESSION_KEY_TOKEN, token.getId());
            log.debug("SSO服务获取到token，重定向returnUrl：{}", returnUrl);
        }
        return ChainRet.BREAK;
    }

    private void redirectUrl(HttpServletResponse response, String url, String paramKey, String paramValue)  {
        StringBuffer stringBuffer = new StringBuffer(url);
        stringBuffer.append(stringBuffer.indexOf(StringPool.QUESTION_MARK) == -1 ? StringPool.QUESTION_MARK : StringPool.AMPERSAND)
                .append(paramKey).append(StringPool.EQUALS)
                .append(URLUtil.encode(paramValue));
        ServletUtils.sendRedirect(response, stringBuffer.toString());
    }

    @Override
    public int getPriority() {
        return -999;
    }

    @Override
    public String getName() {
        return "sso-server";
    }
}
