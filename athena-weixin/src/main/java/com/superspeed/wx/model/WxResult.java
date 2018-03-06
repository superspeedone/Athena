package com.superspeed.common.model;


import com.superspeed.common.kit.JsonKit;

/**
 * 功能描述: 封装ajax返回
 */
public class WxResult {

    /** 标记成功失败，默认0：成功，1：失败(大异常)，2：业务异常 */
    private int code = 0;

    /** 返回的中文消息 */
    private String message;

    /** 成功时携带的数据 */
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 校验错误
     * @return
     */
    public boolean hasError() {
        return this.code != 0;
    }

    /**
     * 设置错误
     * @param message
     * @return
     */
    public WxResult setError(String message) {
        this.message = message;
        this.code = 1;
        return this;
    }

    /**
     * 设置错误
     * @param message
     * @return
     */
    public WxResult setBusinessError(String message) {
        this.message = message;
        this.code = 2;
        return this;
    }

    /**
     * 封装成功时的数据
     * @param data
     * @return WxResult
     */
    public WxResult success(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JsonKit.toJson(this);
    }
}

