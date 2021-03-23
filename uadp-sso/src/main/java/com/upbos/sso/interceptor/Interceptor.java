/*******************************************************************************
 * @(#)Interceptor.java 2019年08月29日 17:11
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/

package com.upbos.sso.interceptor;

import com.upbos.sso.SsoManager;
import com.upbos.sso.entity.ChainRet;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * <b>Application name：</b> Interceptor.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V6.0.0 <br>
 */
public interface Interceptor {

    /**
     * 拦截器名称
     *
     * @return
     */
    String getName();

    /**
     * 拦截器执行链
     *
     * @param request        request包装类
     * @param response        response包装类
     * @return true：执行通过，false：执行不通过
     * @throws Exception
     */
    ChainRet preHandle(HttpServletRequest request, HttpServletResponse response, SsoManager ssoManager);


    default int matchIndex(HttpServletRequest request, List<String> urlPatterns, PathMatcher matcher) {
        int index = -1;
        if (urlPatterns == null) {
            return index;
        }
        if (matcher == null) {
            matcher = new AntPathMatcher();
        }
        for (int i = 0; i < urlPatterns.size(); i++) {
            if (matcher.match(urlPatterns.get(i), request.getRequestURI())) {
                return i;
            }
        }
        return index;
    }

    /**
     * 拦截器的执行优先级，越小执行优先级越高，默认值：-1
     *
     * @return 优先级
     */
    int getPriority();
}