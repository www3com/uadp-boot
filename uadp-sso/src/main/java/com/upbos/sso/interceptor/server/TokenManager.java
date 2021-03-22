package com.upbos.sso.interceptor.server;


import cn.hutool.extra.servlet.ServletUtil;
import com.upbos.sso.SsoManager;
import com.upbos.sso.entity.Token;
import com.upbos.sso.util.JsonUtils;
import com.upbos.sso.ret.RetCode;
import com.upbos.sso.ret.RetData;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wangjz
 */
public class TokenManager {
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_TYPE_TOKEN = "token";
    public static final String PARAM_TYPE_ATTR_SET = "attr_set";
    public static final String PARAM_TYPE_ATTR_GET = "attr_get";
    public static final String PARAM_TOKEN_ID = "tokenId";
    public static final String PARAM_ATTR_KEY = "key";
    public static final String PARAM_ATTR_VALUE = "value";

    private SsoManager ssoManager;

    public TokenManager(SsoManager ssoManager) {
        this.ssoManager = ssoManager;
    }

    public RetData get(HttpServletRequest request) {
        RetData retData = new RetData(RetCode.SUCCESS);
        String type = request.getParameter(PARAM_TYPE);
        if (type == null) {
            retData.setCode(RetCode.PARAMETER_REQUIRED);
            retData.setMsg("缺少请求参数：type");
            return retData;
        }

        String tokenId = request.getParameter(PARAM_TOKEN_ID);
        if (tokenId == null) {
            retData.setCode(RetCode.PARAMETER_REQUIRED);
            retData.setMsg("缺少参数：" + PARAM_TOKEN_ID);
            return retData;
        }

        Token token = ssoManager.getToken(tokenId);
        if (token == null) {
            retData.setCode(RetCode.TOKEN_EXPIRE);
            return retData;
        }

        switch (type) {
            case PARAM_TYPE_TOKEN:
                retData.setData(token);
                break;
            case PARAM_TYPE_ATTR_SET:
                setAttr(request);
                break;
            case PARAM_TYPE_ATTR_GET:
                retData.setData(getAttr(request));
                break;
            default:
                retData.setCode(RetCode.PARAMETER_FORMAT_ERROR);
        }
        return retData;
    }

    @SneakyThrows
    public void setAttr(HttpServletRequest request) {
        String tokenId = request.getParameter(PARAM_TOKEN_ID);
        Map<String, Object> map = JsonUtils.toObject(ServletUtil.getBody(request), Map.class);
        String key = (String) map.get(PARAM_ATTR_KEY);
        Object value = map.get(PARAM_ATTR_VALUE);
        ssoManager.setAttr(tokenId, key, value);
    }

    public Object getAttr(HttpServletRequest request) {
        String tokenId = request.getParameter(PARAM_TOKEN_ID);
        String key = request.getParameter(PARAM_ATTR_KEY);
        return tokenId == null ? null : ssoManager.getAttr(tokenId, key);
    }
}
