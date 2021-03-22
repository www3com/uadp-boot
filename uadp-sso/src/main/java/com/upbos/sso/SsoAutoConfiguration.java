package com.upbos.sso;

import com.upbos.cache.SingleCacheManager;
import com.upbos.cache.props.CacheDetailProp;
import com.upbos.cache.support.CaffeineCacheManager;
import com.upbos.cache.support.RedisCacheManager;
import com.upbos.sso.annotation.ConditionalOnExistProperty;
import com.upbos.sso.cookie.CookieManager;
import com.upbos.sso.cookie.SimpleCookieManager;
import com.upbos.sso.interceptor.ResInterceptor;
import com.upbos.sso.interceptor.TokenInterceptor;
import com.upbos.sso.interceptor.Interceptor;
import com.upbos.sso.props.*;
import com.upbos.sso.storage.SessionKickOutListener;
import com.upbos.sso.storage.StorageManager;
import com.upbos.sso.storage.cache.CacheStorageManager;
import com.upbos.sso.storage.sso.SsoStorageManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(value = {SsoProps.class})
@ConditionalOnClass(SsoFilter.class)
public class SsoAutoConfiguration {

    @Resource
    private SsoProps ssoProps;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 创建cookie管理器
     * @return cookie管理器
     */
    @Bean
    public CookieManager cookieManager() {
        CookieManager cookieManager = new SimpleCookieManager();
        SsoCookieProps cookieProps = ssoProps.getCookie();
        if (cookieProps == null) {
            return cookieManager;
        }
        cookieManager.setTokenName(ssoProps.getToken().getTokenName());
        cookieManager.setMaxAge(cookieProps.getMaxAge());
        cookieManager.setHttpOnly(cookieProps.getHttpOnly());
        cookieManager.setRememberMe(cookieProps.getRememberMe());
        return cookieManager;
    }

    /**
     * 创建caffeine存储器
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "sso", name = "token.storage-type", havingValue = "caffeine")
    public StorageManager caffeineStorageManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaches(getCacheDetailProps());
        CacheStorageManager cacheStorageManager = new CacheStorageManager();
        cacheStorageManager.setSingleCacheManager(cacheManager);
        setCacheStorageProps(cacheStorageManager);
        return cacheStorageManager;
    }

    /**
     * 创建redis存储器
     * @param redisTemplate spring redis操作模板
     * @return redis存储器
     */
    @Bean
    @ConditionalOnProperty(prefix = "sso", name = "token.storage-type", havingValue = "redis")
    public StorageManager redisStorageManager(RedisTemplate redisTemplate) {
        SingleCacheManager redisCacheManager = new RedisCacheManager(redisTemplate, getCacheDetailProps());
        CacheStorageManager cacheStorageManager = new CacheStorageManager();
        cacheStorageManager.setSingleCacheManager(redisCacheManager);
        setCacheStorageProps(cacheStorageManager);
        return cacheStorageManager;
    }

    /**
     * 创建sso客户端存储器
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "sso", name = "storage", havingValue = "sso")
    public StorageManager ssoStorageManager() {
        SsoStorageManager ssoStorageManager = new SsoStorageManager();
        ssoStorageManager.setSsoTokenUrl(ssoProps.getToken().getSsoTokenUrl());
        // 启用本地缓存
        ssoStorageManager.setLocalCacheManager(localCacheManager());
        return ssoStorageManager;
    }


    /**
     * 创建sso管理器
     * @param storageManager 存储器
     * @param cookieManager cookie管理器
     * @return
     */
    @Bean
    public SsoManager ssoManager(StorageManager storageManager, CookieManager cookieManager) {
        SingleSsoManager ssoManager = new SingleSsoManager();
        ssoManager.setStorageManager(storageManager);
        ssoManager.setCookieManager(cookieManager);
        return ssoManager;
    }

    /**
     * 拦截器
     * 根据配置信息初始化token校验拦截器：token
     *
     * @return
     */
    @Bean
    @ConditionalOnExistProperty(prefix = "sso.interceptors", name = "token")
    public Interceptor tokenInterceptor() {
        TokenInterceptor tokenInterceptor = new TokenInterceptor();
        tokenInterceptor.setExpireUrl(ssoProps.getInterceptors().getToken().getExpireUrl());
        tokenInterceptor.setRules(ssoProps.getInterceptors().getToken().getRules());
        return tokenInterceptor;
    }

    /**
     * 拦截器
     * 根据配置信息初始化资源拦截器：res
     *
     * @return
     */
    @Bean
    @ConditionalOnExistProperty(prefix = "sso.interceptors", name = "res")
    public Interceptor resInterceptor() {
        ResInterceptor resInterceptor = new ResInterceptor();
        resInterceptor.setExcludeUrls(ssoProps.getInterceptors().getRes().getExcludeUrls());
        return resInterceptor;
    }

    /**
     * 注册filter
     * @param ssoManager sso管理器
     * @param interceptors 拦截器队列
     * @return filter
     */
    @Bean
    public FilterRegistrationBean registerFilter(SsoManager ssoManager, List<Interceptor> interceptors) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        SsoFilter ssoFilter = new SsoFilter();

        if (ssoProps.getExcludeUrls() != null) {
            ssoFilter.setExcludeUrls(ssoProps.getExcludeUrls());
        }

        ssoFilter.setSsoManager(ssoManager);
        ssoFilter.setInterceptors(interceptors);
        registration.setFilter(ssoFilter);
        registration.addUrlPatterns(ssoProps.getFilterUrl());
        registration.setName(ssoProps.getFilterName());
        registration.setOrder(ssoProps.getOrder());
        return registration;
    }


    /**
     * 设置缓存的相关属性
     * @param cacheStorageManager
     */
    private void setCacheStorageProps(CacheStorageManager cacheStorageManager) {
        cacheStorageManager.setOnlyOne(ssoProps.getToken().getOnlyOne());

        // 启用本地缓存
        if (!ssoProps.getToken().getStorageType().equals("caffeine")) {
            cacheStorageManager.setLocalCacheManager(localCacheManager());
        }

        Map<String, SessionKickOutListener> map = applicationContext.getBeansOfType(SessionKickOutListener.class);
        map.forEach((s, sessionKickOutListener) -> cacheStorageManager.setSessionKickOutListener(sessionKickOutListener));
    }

    /**
     * 生成本地缓存
     * @return 本地缓存
     */
    private SingleCacheManager localCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        CacheDetailProp cacheDetailProp = new CacheDetailProp();
        cacheDetailProp.setCacheName(CacheDetailProp.DEFAULT_CACHE_NAME);
        cacheDetailProp.setExpireTime("1m");
        cacheDetailProp.setExpirePolicy(CacheDetailProp.EXPIRE_POLICY_WRITE);
        cacheManager.setCache(cacheDetailProp);
        return cacheManager;
    }

    private List<CacheDetailProp> getCacheDetailProps() {
        List<CacheDetailProp> detailProps = new ArrayList<>();

        for (SsoTokenTypeProps tokenType : ssoProps.getToken().getTypes()) {
            CacheDetailProp detailProp = new CacheDetailProp();
            detailProp.setCacheName(tokenType.getName());
            detailProp.setExpireTime(tokenType.getExpireTime());
            detailProp.setExpirePolicy(tokenType.getExpirePolicy());
            detailProps.add(detailProp);
        }

        return detailProps;
    }
}
