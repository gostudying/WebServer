package com.lanjian.dispatcher.handler;

import java.io.IOException;

import com.lanjian.exception.InternalServerErrorException;
import com.lanjian.exception.base.ServletException;
import com.lanjian.exception.handler.ExceptionHandler;
import com.lanjian.request.Request;
import com.lanjian.response.Response;
import com.lanjian.servlet.HttpServlet;

/**
 * @explain servlet容器，用于处理单个request请求
 * @author lanjian
 * @date 2019年3月2日
 */
public class RequestHandler implements Runnable {
	private Response response;
	private Request request;
	private HttpServlet servlet;
	private ExceptionHandler exceptionHandler;

	public RequestHandler(Request request, Response response, HttpServlet servlet) {
		this.request = request;
		this.response = response;
		this.servlet = servlet;
	}

	@Override
	public void run() {
		try {
			servlet.service(request, response);
		} catch (IOException e) {
			exceptionHandler.handle(new InternalServerErrorException(), response);
		} catch (ServletException e) {
			exceptionHandler.handle(e, response);
		}
	}
}
