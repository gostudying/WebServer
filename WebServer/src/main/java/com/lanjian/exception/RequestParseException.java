package com.lanjian.exception;

import com.lanjian.constant.HttpStatus;
import com.lanjian.exception.base.ServletException;

/**
 * @explain 请求解析出国
 * @author lanjian
 * @date 2019年3月3日
 */
public class RequestParseException extends ServletException {
	private static final int status = HttpStatus.BadRequest;

	public RequestParseException() {
		super(status);
	}
}
