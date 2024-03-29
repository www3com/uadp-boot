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
@ConfigurationProperties(prefix = "sso")
public class SsoProps {

    /**
     * 过滤器的名称
     */
    private String filterName = "ssoFilter";

    /**
     * 过滤的路径
     */
    private String filterUrl = "/*";

    /**
     * 排除的路径
     */
    private List<String> excludeUrls;

    private SsoCookieProps cookie;

    private Integer order = 1;

}