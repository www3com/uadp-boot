package com.upbos.sso.storage;


import com.upbos.sso.entity.Token;

/**
 * @author wangjz
 */
public interface SessionKickOutListener {
    /**
     * 同一账号，只允许一个用户登录时，监听被踢出的token
     * @param token
     */
    void notice(Token token);
}
