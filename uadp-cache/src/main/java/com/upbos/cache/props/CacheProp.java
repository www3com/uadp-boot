package com.upbos.cache.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <b>Application name：</b> CacheProp.java <br>
 * <b>Application describing： 配置参数</b> <br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2020年2月21日 09:51 <br>
 * <b>@author：</b> <a href="mailto:wjzchina2008@126.com"> Jason </a> <br>
 * <b>version：</b>V6.0.0 <br>
 */
@Data
@ConfigurationProperties(prefix = "cache")
public class CacheProp {

    /**
     * 使用的缓存类型，包括：caffeine、redis
     */
    private String type;

    /**
     * 缓存配置参数
     */
    private List<CacheDetailProp> specs;

}