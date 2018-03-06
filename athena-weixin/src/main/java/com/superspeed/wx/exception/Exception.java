package com.superspeed.common.exception;

/**
 * 自定义异常类
 * @author ynChen
 * @Time 2016-3-23 下午2:55:37
 */
public class Exception extends RuntimeException{
	
	private static final long serialVersionUID = 2690983108141971667L;

	protected String code;

	public String getCode() {
		return this.code;
	}

	public Exception(String message) {
		super(message);
	}

	public Exception(String code, String message) {
		super(message);

		this.code = code;
	}

}
