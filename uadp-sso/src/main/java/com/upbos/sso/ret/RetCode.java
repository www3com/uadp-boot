/*******************************************************************************
 * @(#)Constants.java 2019年08月29日 15:47
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso.ret;

import java.io.Serializable;

/**
 * <b>Application name：</b> RetCode.java <br>
 * <b>Application describing： 返回代码常量</b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2020年04月21日 15:47 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.3.4 <br>
 */
public class RetCode implements Serializable {

    public static RetCode SUCCESS = new RetCode(0, "成功");
    public static RetCode FAIL = new RetCode(-1, "失败");

    public static RetCode UNKNOWN_EXCEPTION = new RetCode(500, "服务器发生内部异常");
    public static RetCode RESOURCE_UNAUTHORIZED = new RetCode(403, "未授权的服务器资源");
    public static RetCode RESOURCE_NOT_EXIST = new RetCode(404, "访问的资源不存在");
    public static RetCode PARAMETER_FORMAT_ERROR = new RetCode(405, "参数格式不符合要求");
    public static RetCode PARAMETER_REQUIRED = new RetCode(406, "缺少请求参数");

    public static RetCode TOKEN_EXPIRE = new RetCode(600, "用户会话失效或过期");
    public static RetCode USER_NOT_EXIST = new RetCode(601, "用户不存在");
    public static RetCode USER_AND_PASSWORD_ERROR = new RetCode(602, "用户密码错误");
    public static RetCode VALIDATE_CODE_ERROR = new RetCode(603, "验证码无效");


    private Integer code;
    private String msg;

    public RetCode() {

    }

    public RetCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}