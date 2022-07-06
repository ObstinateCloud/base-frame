package com.lenged.system.enums;

/**
 * @author
 */
public enum ResponseCode {

    /**
     * 成功
     */
    SUCCESS(200, "SUCCESS", "SUCCESS"),
    ERROR(100, "ERROR", "ERROR"),
    RESULT_NULL(201,"RESULT_NULL","返回值为空"),
    GET_TOKEN_FAIL(401,"token获取失败","token获取失败"),

    ILLEGAL_ARGUMENT(2, "参数错误", "参数错误"),
    MISSING_PARAMETER(20, "缺少必填参数", "缺少必填参数"),

    NEED_LOGIN(10, "需要登录", "需要登录"),

    UNKNOWN_EXCEPTION(-1, "", "未知错误");




    public static ResponseCode getByCode(int code){
        ResponseCode[] values = ResponseCode.values();
        for (int i = 0; i < values.length; i++) {
            ResponseCode responseCode = values[i];
            if (responseCode.code == code){
                return responseCode;
            }
        }
        return null;
    }



    /**
     * code 值
     */
    private final int code;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 用户语言
     */
    private final String userMessage;

    ResponseCode(int code, String desc, String userMessage) {
        this.code = code;
        this.desc = desc;
        this.userMessage = userMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
