package com.lanjian.exception;

import com.lanjian.enums.HttpStatus;
import com.lanjian.exception.base.ServletException;

public class InternalServerErrorException extends ServletException {
	private static HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	public InternalServerErrorException() {
		super(status);
	}

}
