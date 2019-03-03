package com.lanjian.exception;

import com.lanjian.constant.HttpStatus;
import com.lanjian.exception.base.ServletException;

public class BadRequestException extends ServletException {
	private static int status = HttpStatus.BadRequest;

	public BadRequestException() {
		super(status);
	}

}
