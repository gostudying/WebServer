package com.lanjian.exception.base;

import com.lanjian.constant.HttpStatus;

/**
 * @explain 根异常，异常和状态码一对一关系
 * @author lanjian
 * @date 2019年3月2日
 */
public class ServletException extends Exception {
	private HttpStatus status;

	public ServletException(HttpStatus status) {
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
