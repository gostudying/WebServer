package com.lanjian.dispatcher.handler;

import java.io.IOException;

import com.lanjian.request.Request;
import com.lanjian.response.Response;
import com.lanjian.servlet.Servlet;

/**
 * @explain 用于处理单个request请求
 * @author lanjian
 * @date 2019年3月2日
 */
public class RequestHandler implements Runnable {
	private Response response;
	private Request request;
	private Servlet servlet;

	public RequestHandler(Request request, Response response, Servlet servlet) {
		this.request = request;
		this.response = response;
		this.servlet = servlet;
	}

	@Override
	public void run() {
		try {
			servlet.service(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
