package com.upbos.sso.entity;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.upbos.sso.exception.SsoException;
import com.upbos.sso.exception.SsoExceptionCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;


/**
 * <b>Application name：</b> Token.java <br>
 * <b>Application describing： Token 票据</b> <br>
 * <b>Copyright：</b> Copyright &copy; 2018 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2018年09月24日 10:41 <br>
 * <b>author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V1.0.0 <br>
 */
@Data
@Slf4j
public class Token implements Serializable {

    /**
     * 主键 ID
     */
    private String id;

    /**
     * token 类型
     */
    private String type;

    /**
     * 用户id
     */
    private String uid;


    private Map<String, Object> data;


    public Token() {
        super();
    }

    public Token(String id, String type, String uid, Map<String, Object> data) {
        this.id = id;
        this.type = type;
        this.uid = uid;
        this.data = data;
    }

    public static final String TYPE_WEB = "web";

    public static final String TYPE_MOBILE = "mobile";
}