package com.lanjian.exception;

import com.lanjian.constant.HttpStatus;
import com.lanjian.exception.base.ServletException;

/**
 * @explain 请求数据不合法
 * @author lanjian
 * @date 2019年3月3日
 */
public class RequestInvalidException extends ServletException {
	private static final int status = HttpStatus.BadRequest;

	public RequestInvalidException() {
		super(status);
	}
}
