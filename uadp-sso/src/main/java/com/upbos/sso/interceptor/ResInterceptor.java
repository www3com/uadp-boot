package com.upbos.sso.interceptor;


import cn.hutool.http.HttpStatus;
import com.upbos.sso.SsoManager;
import com.upbos.sso.constant.Constants;
import com.upbos.sso.entity.ChainRet;
import com.upbos.sso.entity.Token;
import com.upbos.sso.ret.RetCode;
import com.upbos.sso.ret.RetData;
import com.upbos.sso.util.ServletUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <b>Application name：</b> TokenValidateInterceptor.java <br>
 * <b>Application describing： 校验Token是否有效 </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2018 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2018年09月24日 12:42 <br>
 * <b>author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V1.0.0 <br>
 */
public class ResInterceptor implements Interceptor {

    private List<String> excludeUrls;

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public String getName() {
        return "res";
    }

    @Override
    public ChainRet preHandle(HttpServletRequest request, HttpServletResponse response, SsoManager ssoManager) {

        Token token = ssoManager.getToken(request);

        if (token == null) {
            return ChainRet.BREAK;
        }


        PathMatcher matcher = new AntPathMatcher();
        String requestUri = request.getRequestURI();


        // 放行例外页面
        if (matchIndex(request, excludeUrls, matcher) > -1) {
            return ChainRet.NEXT;
        }

        List<String> resList = ssoManager.getAttr(token.getId(), Constants.SESSION_KEY_PRIVILEGE);
        if (resList != null) {
            for (String res : resList) {
                if (matcher.match(res, requestUri)) {
                    return ChainRet.NEXT;
                }
            }
        }

        RetData retData = new RetData(RetCode.RESOURCE_UNAUTHORIZED);
        response.setStatus(HttpStatus.HTTP_INTERNAL_ERROR);
        ServletUtils.write(response, retData);
        return ChainRet.BREAK;
    }


    @Override
    public int getPriority() {
        return 2;
    }

}
