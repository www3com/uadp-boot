package com.upbos.sso.exception;

/**
 * @author wangjz
 */

public enum SsoExceptionCode {
    /**
     * token为空
     */
    TOKEN_NULL(1000, "token为空"),
    /**
     * tokenId为空
     */
    TOKEN_ID_NULL(1001, "tokenId为空"),
    /**
     * tokenId格式解析异常
     */
    TOKEN_ID_FORMAT(1002, "解析tokenId格式异常"),
    /**
     * tokenId的type异常
     */
    TOKEN_ID_GENERATE_TYPE(1003, "token类型为空或者出现下划线"),

    /**
     * tokenId的uid异常
     */
    TOKEN_ID_GENERATE_UID(1004, "token用户id为空或者出现下划线"),

    /**
     * 拦截器client未配置ssoServerUrl异常
     */
    INTERCEPTOR_SSO_SERVER_URL_NULL(1101, "sso客户端未配置token服务地址：ssoTokenUrl");

    private int code;
    private String msg;

    SsoExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
