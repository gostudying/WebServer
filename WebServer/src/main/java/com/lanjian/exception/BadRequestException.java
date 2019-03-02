package com.lanjian.exception;

import com.lanjian.constant.HttpStatus;
import com.lanjian.exception.base.ServletException;

public class BadRequestException extends ServletException {
	private static HttpStatus status = HttpStatus.BAD_REQUEST;

	public BadRequestException() {
		super(status);
	}

}
