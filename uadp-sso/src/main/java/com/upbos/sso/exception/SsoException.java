package com.upbos.sso.exception;


/**
 * @author wangjz
 */
public class SsoException extends RuntimeException {
    private static final long serialVersionUID = 6057602589533840889L;
    private int code;

    public SsoException(SsoExceptionCode ssoExceptionCode) {
        super(new StringBuffer("[").append(ssoExceptionCode.getCode()).append("]").append(ssoExceptionCode.getMsg()).toString());
        this.code = ssoExceptionCode.getCode();
    }

    public SsoException(Throwable throwable) {
        super(throwable);
    }

    public SsoException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public SsoException(int code, String msg) {
        super(new StringBuffer("[").append(code).append("]").append(msg).toString());
        this.code = code;
    }


    public int getStatus() {
        return this.code;
    }
}
