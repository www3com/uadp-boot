package com.upbos.sso.interceptor;


import com.upbos.sso.SsoManager;
import com.upbos.sso.entity.ChainRet;
import com.upbos.sso.props.SsoInterceptorTokenRuleProps;
import com.upbos.sso.util.ServletUtils;
import com.upbos.sso.ret.RetCode;
import com.upbos.sso.ret.RetData;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Application name：</b> TokenValidateInterceptor.java <br>
 * <b>Application describing： 校验Token是否有效 </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2018 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2018年09月24日 12:42 <br>
 * <b>author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V1.0.0 <br>
 */
public class TokenInterceptor implements Interceptor {


    private String expireUrl = "";

    private List<SsoInterceptorTokenRuleProps> rules = new ArrayList<>();


    public void setExpireUrl(String expireUrl) {
        this.expireUrl = expireUrl;
    }

    public void setRules(List<SsoInterceptorTokenRuleProps> rules) {
        if (rules != null) {
            this.rules = rules;
        }
    }


    @Override
    public String getName() {
        return "token";
    }

    @Override
    public ChainRet preHandle(HttpServletRequest request, HttpServletResponse response, SsoManager ssoManager) {

        String requestUri = request.getRequestURI();
        PathMatcher matcher = new AntPathMatcher();

        // 放行会话过期页面expireUrl
        if (matcher.match(expireUrl, requestUri)) {
            return ChainRet.SKIP;
        }

        // 放行规则中的会话过期页面expireUrl
        for (SsoInterceptorTokenRuleProps rule : rules) {
            if (matcher.match(rule.getExpireUrl(), requestUri)) {
                return ChainRet.SKIP;
            }
        }


        // 验证会话是否有效
        if (ssoManager.validateToken(request)) {
            return ChainRet.NEXT;
        }

        //session不存在，重新定向session失效页面
        RetData retData = new RetData(RetCode.TOKEN_EXPIRE);

        if (ServletUtils.isAjax(request)) {
            response.setStatus(ServletUtils.HTTP_STATUS_500);
            ServletUtils.write(response, retData);
            return ChainRet.BREAK;
        }

        // 重定向到规则中的过期页面

        for (SsoInterceptorTokenRuleProps rule : rules) {
            for (String url : rule.getIncludeUrls()) {
                if (matcher.match(url, requestUri)) {
                    ServletUtils.sendRedirect(response, rule.getExpireUrl());
                    return ChainRet.BREAK;
                }
            }
        }

        // 定义的规则中没有找到重定向页面，则重定向到默认页面
        ServletUtils.sendRedirect(response, this.expireUrl);
        return ChainRet.BREAK;
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
