package com.lenged.system.dto.base;


import com.lenged.system.enums.ResponseCode;
import io.swagger.annotations.ApiModelProperty;

import static com.lenged.system.enums.ResponseCode.GET_TOKEN_FAIL;


/**
 * 返回数据格式
 *
 * @param <T>
 */
public class BaseResponse<T> {


    /**
     * 200 成功，否则表示失败
     */
    @ApiModelProperty("200 成功，否则表示失败")
    private int resultCode = 200;

    /**
     * 结果描述/消息
     */
    @ApiModelProperty("结果描述/消息/提示")
    private String msg = "ok";


    /**
     * 返回的数据
     */
    @ApiModelProperty("返回的数据")
    private T data;


    public BaseResponse() {
    }

    private BaseResponse(int status) {
        this.resultCode = status;
    }

    private BaseResponse(int status, T data) {
        this.resultCode = status;
        this.data = data;
    }

    private BaseResponse(int status, String msg, T data) {
        this.resultCode = status;
        this.msg = msg;
        this.data = data;
    }

    private BaseResponse(int status, String msg) {
        this.resultCode = status;
        this.msg = msg;
    }


    /**
     * 判断接口是否响应成功
     * @return
     */
    public Boolean ok() {
        if (this.getResultCode() == ResponseCode.SUCCESS.getCode()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public Boolean fail() {
        return !ok();
    }


    public static <T> BaseResponse<T> create(ResponseCode responseCode) {
        return new BaseResponse<T>(responseCode.getCode(), responseCode.getDesc());
    }


    public static <T> BaseResponse<T> success() {
        return new BaseResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> BaseResponse<T> success(String msg) {
        return new BaseResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> BaseResponse<T> successData(T data) {
        return new BaseResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> BaseResponse<T> success(String msg, T data) {
        return new BaseResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> BaseResponse<T> error() {
        return new BaseResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> BaseResponse<T> error(int code, String msg) {
        return new BaseResponse<T>(code, msg);
    }

    public static <T> BaseResponse<T> error(int code, String msg, T data) {
        return new BaseResponse<T>(code, msg, data);
    }

    public static <T> BaseResponse<T> error(String errorMessage) {
        return new BaseResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> BaseResponse<T> tokenByError() {
        return create(GET_TOKEN_FAIL);
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
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
}
