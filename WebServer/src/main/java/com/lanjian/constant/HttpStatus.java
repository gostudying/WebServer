package com.lanjian.constant;

/**
 * @author lanjian
 * @date 2019年2月28日
 */
public enum HttpStatus {
	OK(200, "OK"), NOT_FOUND(404, "NOT_FOUND"), INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),
	BAD_REQUEST(400, "BAD_REQUEST");
	private int code;
	private String msg;

	HttpStatus(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
