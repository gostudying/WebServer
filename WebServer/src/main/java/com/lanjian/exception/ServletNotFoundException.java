package com.lanjian.exception;

import com.lanjian.constant.HttpStatus;
import com.lanjian.exception.base.ServletException;

public class ServletNotFoundException extends ServletException {
	private static final long serialVersionUID = 1L;
	private static int status = HttpStatus.NotFound;

	public ServletNotFoundException() {
		super(status);
	}

}
