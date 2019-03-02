package com.lanjian.exception;

import com.lanjian.constant.HttpStatus;
import com.lanjian.exception.base.ServletException;

public class ServletNotFoundException extends ServletException {
	private static HttpStatus status = HttpStatus.NOT_FOUND;

	public ServletNotFoundException() {
		super(status);
	}

}
