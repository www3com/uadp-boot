/*******************************************************************************
 * @(#)DaoConfigProperties.java 2018年10月03日 09:51
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso.props;


import lombok.Data;

/**
 * <b>Application name：</b> DaoConfigProperties.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2018 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2018年10月03日 09:51 <br>
 * <b>author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V4.1.0 <br>
 * @author wangjz
 */

@Data
public class SsoCookieProps {

    private Boolean httpOnly;

    private Integer maxAge;

    private Boolean rememberMe;

}