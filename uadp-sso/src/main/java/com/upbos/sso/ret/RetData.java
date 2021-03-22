/*******************************************************************************
 * @(#)RetData.java 2017年04月13日 10:18 
 * Copyright 2017 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.sso.ret;

/*******************************************************************************
 * @(#)RetData.java 2020年03月11日 15:47
 * Copyright 2020 upbos.com. All rights reserved.
 *******************************************************************************/

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>Application name：</b> RetData.java <br>
 * <b>Application describing： 返回数据包裹类</b> <br>
 * <b>Copyright：</b> Copyright &copy; 2020 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2020年03月11日 15:47 <br>
 * <b>@author：</b> <a href="mailto:jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.3.1 <br>
 */

public class RetData<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public RetData() {
        this.code = RetCode.SUCCESS.getCode();
        this.msg = RetCode.SUCCESS.getMsg();
    }

    public RetData(RetCode retCode) {
        this.code = retCode.getCode();
        this.msg = retCode.getMsg();
    }


    public void putData(String key, Object value) {
        if (data == null) {
            data = (T) new HashMap(16);
        }
        if (data instanceof Map) {
            ((Map<String, Object>) data).put(key, value);
        }
    }

    public Object getData(String key) {
        if (data instanceof Map) {
            return ((Map<String, Object>) data).get(key);
        }
        return data;
    }

    public void removeData(String key) {
        if (data instanceof Map) {
            ((Map<String, Object>) data).remove(key);
        }
    }

    /**
     * 清除数据
     */
    public void clearData() {
        if (data instanceof Map) {
            ((Map<String, Object>) data).clear();
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setCode(RetCode retCode) {
        this.code = retCode.getCode();
        this.msg = retCode.getMsg();
    }

    public void setCode(RetCode retCode, String msg) {
        this.code = retCode.getCode();
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 请求响应成功，返回的报文
     *
     * @return {code: 0, data: {}, msg: "成功"}
     */
    public static RetData success() {
        return ret(RetCode.SUCCESS);
    }



    /**
     * 请求响应成功，返回的报文
     *
     * @param msg 返回的消息
     * @return {code: 0, data: {}, msg: 【返回的消息】}
     */
    public static RetData success(String msg) {
        return ret(RetCode.SUCCESS, msg);
    }

    /**
     * 请求响应成功，返回的报文
     *
     * @param msg 返回的消息
     * @return {code: 0, data: {}, msg: 【返回的消息】}
     */
    public static RetData success(String msg, Object data) {
        return ret(RetCode.SUCCESS, msg, data);
    }


    /**
     * 请求响应失败，返回的报文
     *
     * @return {code: -1, data: {}, msg: "失败"}
     */
    public static RetData fail() {
        return ret(RetCode.FAIL);
    }

    /**
     * 请求响应失败，返回的报文
     *
     * @param msg 返回的消息
     * @return {code: -1, data: {}, msg: 【返回的消息】}
     */
    public static RetData fail(String msg) {
        return ret(RetCode.FAIL, msg);
    }


    public static RetData ret(RetCode retCode, String msg, Object data) {
        RetData retData = new RetData(retCode);
        retData.setData(data);
        retData.setMsg(msg != null ? msg : retData.getMsg());
        return retData;
    }

    /**
     * 请求响应失败，返回的报文
     *
     * @param retCode 返回的编码
     * @param msg     返回的信息
     * @return {code: 0, data: 【返回的数据】, msg: "失败"}
     */
    public static RetData ret(RetCode retCode, String msg) {
        return ret(retCode, msg, null);
    }

    /**
     * 请求响应失败，返回的报文
     *
     * @param data 返回的数据
     * @return {code: 0, data: 【返回的数据】, msg: "失败"}
     */
    public static RetData ret(RetCode retCode, Object data) {
        return ret(retCode, null, data);
    }

    /**
     * 请求响应失败，返回的报文
     *
     * @param retCode 返回的编码
     * @return {code: 0, data: 【返回的数据】, msg: "失败"}
     */
    public static RetData ret(RetCode retCode) {
        return ret(retCode, null, null);
    }

    public static RetData ret(Integer code, String msg, Object data) {
        RetData retData = new RetData();
        retData.setCode(code);
        retData.setMsg(msg);
        retData.setData(data);
        return retData;
    }

    public static RetData ret(Integer code, String msg) {
        return ret(code, msg, null);
    }
}