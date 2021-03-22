package com.upbos.sso.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.upbos.sso.ret.RetData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletUtils {

    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";


    private static final String AJAX_KEY = "X-Requested-With";

    private static final String AJAX_VALUE = "XMLHttpRequest";

    public static final int HTTP_STATUS_500 = 500;

    public static boolean isAjax(HttpServletRequest request) {
        String ajaxHeader = ServletUtil.getHeader(request, AJAX_KEY, CharsetUtil.defaultCharset());
        return AJAX_VALUE.equals(ajaxHeader) ? true : false;
    }

    public static void write(HttpServletResponse response, RetData retData) {
        write(response, JsonUtils.toJson(retData));
    }

    public static void write(HttpServletResponse response, String content) {
        ServletUtil.write(response, content, CONTENT_TYPE_JSON);
    }

    public static void sendRedirect(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
