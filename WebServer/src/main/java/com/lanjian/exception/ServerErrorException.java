package com.lanjian.exception;

import com.lanjian.constant.HttpStatus;
import com.lanjian.exception.base.ServletException;

public class ServerErrorException extends ServletException {
	private static int status = HttpStatus.ServerError;

	public ServerErrorException() {
		super(status);
	}

}
