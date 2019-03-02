package com.lanjian.exception.handler;

import java.io.IOException;

import com.lanjian.exception.base.ServletException;
import com.lanjian.response.Response;

/**
 * @explain 会根据异常对应的状态码设置response的状态
 * @author lanjian
 * @date 2019年3月2日
 */
public class ExceptionHandler {
	public void handle(ServletException e, Response response) {
		e.printStackTrace();
		try {
			response.pushToClient(e.getStatus());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
