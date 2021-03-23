/*******************************************************************************
 * @(#)DaoConfigProperties.java 2018年10月03日 09:51
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <b>Application name：</b> DaoConfigProperties.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2018 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2018年10月03日 09:51 <br>
 * <b>author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V4.1.0 <br>
 */

@Data
@ConfigurationProperties(prefix = "sso.token")
public class SsoTokenProps {

    /**
     * 同一用户多次登录，只保留最后一次登录session
     */
    private Boolean onlyOne = false;

    /**
     * token ID 存储在http cookie或者header中的名称
     */
    private String tokenName = "sid";

    /**
     * token持久化的方式，支持方式：redis、caffeine、sso
     */
    private String storageType = "caffeine";

    /**
     * 过期时间，默认1小时
     */
    private String expireTime = "1h";

    /**
     * Token信息获取的url，配合storageType使用，当storageType等于sso时，应当配置此参数
     */
    private String ssoTokenUrl;

    /**
     * 多个token类型的过期时间设置
     */
    private List<SsoTokenTypeProps> types;

}