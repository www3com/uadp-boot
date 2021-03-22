/*******************************************************************************
 * @(#)HttpStorageManager.java 2019年08月29日 17:11
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/

package com.upbos.sso.storage.sso;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.upbos.cache.SingleCacheManager;
import com.upbos.cache.props.CacheDetailProp;
import com.upbos.sso.entity.Token;
import com.upbos.sso.interceptor.server.TokenManager;
import com.upbos.sso.storage.StorageManager;
import com.upbos.sso.util.JsonUtils;
import com.upbos.sso.ret.RetData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * <b>Application name：</b> HttpStorageManager.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.0.0 <br>
 */
@Slf4j
public class SsoStorageManager implements StorageManager {


    private String ssoTokenUrl;

    private SingleCacheManager localCacheManager;

    public void setLocalCacheManager(SingleCacheManager localCacheManager) {
        this.localCacheManager = localCacheManager;
    }

    public void setSsoTokenUrl(String ssoTokenUrl) {
        this.ssoTokenUrl = ssoTokenUrl;
    }

    @Override
    public Token saveToken(String type, String uid, Map<String, Object> data) {
        throw new RuntimeException("http方式不支持Token保存操作");
    }

    @Override
    public void removeToken(String tokenId) {
        throw new RuntimeException("http方式不支持Token删除操作");
    }


    @SneakyThrows
    @Override
    public Token getToken(String tokenId) {
        if (tokenId == null) {
            return null;
        }

        Token token = localCacheManager == null ? null : localCacheManager.getCache(CacheDetailProp.DEFAULT_CACHE_NAME).get(tokenId);
        if (token == null) {

            Map<String, Object> params = new HashMap<String, Object>(2) {{
                put(TokenManager.PARAM_TYPE, TokenManager.PARAM_TYPE_TOKEN);
                put(TokenManager.PARAM_TOKEN_ID, tokenId);
            }};
            long s = System.currentTimeMillis();
            String ret = HttpUtil.get(ssoTokenUrl, params);
            long e = System.currentTimeMillis();
            log.debug("读取http缓存耗时{}毫秒, tokenId:{}", e - s, tokenId);
            if ("".equals(ret)) {
                return null;
            }
            RetData<Token> retData = JsonUtils.toCollection(ret, new TypeReference<RetData<Token>>() {});
            token = retData.getData();


            if (localCacheManager != null && token != null) {
                localCacheManager.getCache(CacheDetailProp.DEFAULT_CACHE_NAME).set(tokenId, token);
            }
        }
        return token;
    }



    @Override
    public void setAttr(String tokenId, String key, Object value) {
        throw new RuntimeException("http方式不支持setAttr操作");
    }

    @Override
    public void removeAttr(String tokenId, String key) {
        throw new RuntimeException("http方式不支持removeAttr操作");
    }

    @Override
    public <T> T getAttr(String tokenId, String key) {
        throw new RuntimeException("http方式不支持getAttr操作");
    }


}
