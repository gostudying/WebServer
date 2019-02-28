package com.lanjian.enums;

/**
 * @author lanjian
 * @date 2019年2月28日
 */
public enum HttpStatus {
	OK(200), NOT_FOUND(404), INTERNAL_SERVER_ERROR(500), BAD_REQUEST(400), MOVED_TEMPORARILY(302);
	private int code;

	HttpStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
