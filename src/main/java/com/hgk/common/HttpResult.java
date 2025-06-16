package com.hgk.common;


import java.io.Serializable;

/**
 * <pre>
 *  描述:统一请求响应体
 * </pre>
 *
 * @author Chris(wangchao)
 * @version 1.0.0
 * @date 2019/08/13 10:23
 */
public class HttpResult<T> implements Serializable {
    private static final long serialVersionUID = 3198797131120684503L;

    /**
     * http响应码,200:成功,500:失败
     */
    private Integer code;

    /**
     * http响应描述
     */
    private String message;

    /**
     * http响应数据体
     */
    private T data;

    private HttpResult(T data) {
        this(ResponseCode.SUCCESS, data);
    }

    private HttpResult(ResponseCode responseCode, T data) {
        this.data = data;
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    private HttpResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功响应
     *
     * @param <T> 泛型结果
     * @return
     */
    public static <T> HttpResult<T> returnSuccess() {
        return new HttpResult<>(null);
    }

    /**
     * 返回成功响应，自定义响应体
     *
     * @param data 返回数据
     * @param <T>  泛型结果
     * @return
     */
    public static <T> HttpResult<T> returnSuccess(T data) {
        return new HttpResult<>(data);
    }

    /**
     * 返回成功响应，自定义消息和data
     *
     * @param message 消息
     * @param data    返回数据
     * @param <T>     泛型结果
     * @return
     */
    public static <T> HttpResult<T> returnSuccess(String message, T data) {
        return new HttpResult<>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 返回失败响应
     *
     * @param <T> 泛型结果
     * @return
     */
    public static <T> HttpResult<T> returnFail() {
        return new HttpResult<>(ResponseCode.FAIL, null);
    }

    /**
     * 返回失败响应，自定义响应体
     *
     * @param responseCode 响应码
     * @param data         返回数据
     * @param <T>          泛型结果
     * @return
     */
    public static <T> HttpResult<T> returnFail(ResponseCode responseCode, T data) {
        return new HttpResult<>(responseCode, data);
    }

    /**
     * 返回失败响应,自定义消息和data
     *
     * @param message 自定义消息
     * @param data    返回数据
     * @return
     */
    public static <T> HttpResult<T> returnFail(String message, T data) {
        return new HttpResult<>(ResponseCode.FAIL.getCode(), message, data);
    }

    /**
     * 返回失败响应,自定义消息
     *
     * @param message 自定义消息
     * @return
     */
    public static <T> HttpResult<T> returnFail(String message) {
        return returnFail(message, null);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "";
    }
}
