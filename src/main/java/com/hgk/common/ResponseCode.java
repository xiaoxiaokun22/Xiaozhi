package com.hgk.common;


/**
 * <pre>
 *  描述: 响应分类
 * </pre>
 *
 * @author Chris(wangchao)
 * @version 1.0.0
 * @date 2019/08/13 10:26
 */
public enum ResponseCode {

    /**
     * 默认成功响应
     */
    SUCCESS(200, "success"),

    /**
     * 默认失败响应
     */
    FAIL(500, "fail");


    private Integer code;
    private String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseCode getEnumByCode(Integer code) {
        if (null == code) {
            return null;
        }

        ResponseCode responseCode;

        for (int i = 0; i < ResponseCode.values().length; i++) {
            responseCode = ResponseCode.values()[i];
            if (responseCode.code.equals(code)) {
                return responseCode;
            }
        }
        return null;
    }
}
