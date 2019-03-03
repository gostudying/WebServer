package com.lanjian.exception.base;

/**
 * @explain 根异常，异常有一个状态码
 * @author lanjian
 * @date 2019年3月2日
 */
public class ServletException extends Exception {
	private int status;

	public ServletException(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}
