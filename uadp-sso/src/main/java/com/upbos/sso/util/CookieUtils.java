package com.upbos.sso.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
    /**
     * 浏览器关闭时自动删除
     */
    public final static int CLEAR_BROWSER_IS_CLOSED = -1;

    /**
     * 立即删除
     */
    public final static int CLEAR_IMMEDIATELY_REMOVE = 0;

    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response, null, null, name, value, CLEAR_BROWSER_IS_CLOSED, false);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        setCookie(response, null, null, name, value, maxAge, false);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, boolean httpOnly) {
        setCookie(response, null, null, name, value, CLEAR_BROWSER_IS_CLOSED, false);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge, boolean httpOnly) {
        setCookie(response, null, null, name, value, maxAge, httpOnly);
    }

    public static void setCookie(HttpServletResponse response, String domain, String path, String name, String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        if (!isEmpty(domain)) {
            cookie.setDomain(domain);
        }
        if (isEmpty(path)) {
            cookie.setPath("/");
        }

        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public static void removeCookie(HttpServletResponse response, String cookieName) {
        setCookie(response, cookieName, "", CLEAR_IMMEDIATELY_REMOVE);
    }

    private static boolean isEmpty(String value) {
        if (value != null && !"".equals(value)) {
            return false;
        } else {
            return true;
        }
    }
}
