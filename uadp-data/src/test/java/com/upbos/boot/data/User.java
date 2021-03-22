/*******************************************************************************
 * @(#)User.java 2018年10月03日 14:19
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.boot.data;

import com.upbos.data.persistence.annotation.Column;
import com.upbos.data.persistence.annotation.Table;
import lombok.Data;


/**
 * <b>Application name：</b> User.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2018 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2018年10月03日 14:19 <br>
 * <b>author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V4.1.0 <br>
 */
@Data
@Table("upm_user")
public class User {
    @Column
    private Integer uid;

    @Column
    private String name;

    @Column("login_name")
    private String loginName;

    @Column
    private String password;
}