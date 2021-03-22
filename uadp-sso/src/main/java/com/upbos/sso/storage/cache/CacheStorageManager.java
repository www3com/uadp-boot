
package com.upbos.sso.storage.cache;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.upbos.cache.SingleCacheManager;
import com.upbos.cache.props.CacheDetailProp;

import com.upbos.sso.entity.Token;
import com.upbos.sso.storage.SessionKickOutListener;
import com.upbos.sso.storage.StorageManager;
import com.upbos.sso.util.JsonUtils;
import com.upbos.sso.util.StringPool;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;

import java.util.Map;


/**
 * <b>Application name：</b> RedisCacheStorageManager.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2020年03月12日 17:11 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.3.2 <br>
 */
public class CacheStorageManager implements StorageManager {

    private SingleCacheManager singleCacheManager;

    private SingleCacheManager localCacheManager;

    private SessionKickOutListener sessionKickOutListener;


    private boolean onlyOne = false;

    public void setOnlyOne(boolean onlyOne) {
        this.onlyOne = onlyOne;
    }

    public void setSessionKickOutListener(SessionKickOutListener listener) {
        this.sessionKickOutListener = listener;
    }


    public void setLocalCacheManager(SingleCacheManager localCacheManager) {
        this.localCacheManager = localCacheManager;
    }

    public void setSingleCacheManager(SingleCacheManager singleCacheManager) {
        this.singleCacheManager = singleCacheManager;
    }


    @Override
    public Token saveToken(String type, String uid, Map<String, Object> data) {

        String tokenId = generateTokenId(type, uid);

        if (sessionKickOutListener != null) {
            Token cacheToken = this.getToken(tokenId);
            if (cacheToken != null) {
                sessionKickOutListener.notice(cacheToken);
            }
        }
        Token token = new Token(tokenId, type, uid, data);
        String tokenJson = JsonUtils.toJson(token);

        CacheToken cacheToken = parseTokenId(tokenId);

        singleCacheManager.getCache(cacheToken.getCacheName()).putForHash(cacheToken.getKey(), TOKEN_KEY, tokenJson);
        return token;
    }


    @Override
    public void removeToken(String tokenId) {
        if (localCacheManager != null) {
            localCacheManager.getCache(CacheDetailProp.DEFAULT_CACHE_NAME).remove(tokenId);
        }

        CacheToken cacheToken = parseTokenId(tokenId);
        singleCacheManager.getCache(cacheToken.getCacheName()).remove(cacheToken.getKey());
    }

    @Override
    public Token getToken(String tokenId) {
        if (StrUtil.hasBlank(tokenId)) {
            return null;
        }

        // 先从本地缓存中获取token, 如果未找到，则从共享缓存中获取token
        Token token = localCacheManager == null ?
                null : localCacheManager.getCache(CacheDetailProp.DEFAULT_CACHE_NAME).get(tokenId);
        if (token == null) {
            CacheToken cacheToken = parseTokenId(tokenId);
            String tokenJson = singleCacheManager.getCache(cacheToken.getCacheName()).getForHash(cacheToken.getKey(), TOKEN_KEY);
            token = tokenJson == null ? null : JsonUtils.toObject(tokenJson, Token.class);
        }
        
        // 更新本地缓存
        if (localCacheManager != null && token != null) {
            localCacheManager.getCache(CacheDetailProp.DEFAULT_CACHE_NAME).set(tokenId, token);
        }

        return token;
    }


    @Override
    public void setAttr(String tokenId, String key, Object value) {
        CacheToken cacheToken = parseTokenId(tokenId);
        singleCacheManager.getCache(cacheToken.getCacheName()).putForHash(cacheToken.getKey(), key, value);
    }

    @Override
    public void removeAttr(String tokenId, String key) {
        CacheToken cacheToken = parseTokenId(tokenId);
        singleCacheManager.getCache(cacheToken.getCacheName()).removeForHash(cacheToken.getKey(), key);
    }

    @Override
    public <T> T getAttr(String tokenId, String key) {
        CacheToken cacheToken = parseTokenId(tokenId);
        return singleCacheManager.getCache(cacheToken.getCacheName()).getForHash(cacheToken.getKey(), key);
    }

    private String generateTokenId(String type, String uid) {
        String d = RandomUtil.randomString(RANDOM_NUMBER_LENGTH);
        String tokenId = StrUtil.format("{}_{}_{}", type, uid, d);
        return Base64.encode(tokenId);
    }

    private CacheToken parseTokenId(String tokenId) {
        String parsedTokenId = Base64.decodeStr(tokenId);
        String[] tokenIds = parsedTokenId.split(String.valueOf(StrUtil.C_UNDERLINE));
        if (tokenIds.length != PART_LENGTH) {
            return null;
        }

        String key = onlyOne ? tokenIds[1] : tokenIds[1] + StringPool.COLON + tokenIds[2];

        CacheToken token = new CacheToken(tokenIds[0], key);
        return token;
    }
    
    @Data
    class CacheToken {

        private String cacheName;

        private String key;

        CacheToken(String cacheName, String key) {
            this.cacheName = cacheName;
            this.key = key;
        }
    }

    public static final int RANDOM_NUMBER_LENGTH = 8;

    public static final int PART_LENGTH = 3;

    private static final String TOKEN_KEY = "_token";
}
