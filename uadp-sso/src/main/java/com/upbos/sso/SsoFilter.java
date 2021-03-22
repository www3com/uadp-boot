/*******************************************************************************
 * @(#)SsoFilter.java 2019年08月29日 17:11
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso;

import cn.hutool.core.collection.CollectionUtil;
import com.upbos.sso.entity.ChainRet;
import com.upbos.sso.interceptor.Interceptor;
import com.upbos.sso.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <b>Application name：</b> SsoFilter.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.0.0 <br>
 */
@Slf4j
public class SsoFilter implements Filter {


    /**
     * 不需要session验证的页面
     */
    private List<String> excludeUrls = new ArrayList<String>(Arrays.asList("/**/public/**", "/**/*.css", "/**/*.js", "/**/*.jpg", "/**/favicon.ico"));
    /**
     * 自定义页面过滤规则
     */
    private List<Interceptor> interceptors;

    private SsoManager ssoManager;


    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls.addAll(excludeUrls);
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
        // 拦截器优先级排序
        Collections.sort(this.interceptors, Comparator.comparingInt(interceptor -> interceptor.getPriority()));
    }

    public void setSsoManager(SsoManager ssoManager) {
        this.ssoManager = ssoManager;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //例外资源不进行校验 或者没有配置拦截器，直接放行
        if (filterExcludeUrl(request) || CollectionUtil.isEmpty(interceptors)) {
            filterChain.doFilter(request, response);
            return;
        }

        //执行用户自定义拦截器
        for (Interceptor interceptor : interceptors) {
            ChainRet ret = interceptor.preHandle(request, response, ssoManager);
            if (ret == ChainRet.BREAK) {
                return;
            } else if (ret == ChainRet.SKIP) {
                break;
            } else {
                continue;
            }
        }

        filterChain.doFilter(request, response);
    }


    public boolean filterExcludeUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        PathMatcher matcher = new AntPathMatcher();
        if (StringPool.SLASH.equals(url) || url.equals(contextPath + StringPool.SLASH) || this.excludeUrls == null) {
            return true;
        }

        for (String eUrl : excludeUrls) {
            if (matcher.match(eUrl, url)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }


}