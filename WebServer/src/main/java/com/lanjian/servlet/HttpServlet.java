package com.lanjian.servlet;

import java.io.IOException;

import com.lanjian.constant.RequestMethod;
import com.lanjian.exception.base.ServletException;
import com.lanjian.request.Request;
import com.lanjian.response.Response;

/**
 * @explain 用于处理HTTP请求
 * @author lanjian
 * @date 2019年3月2日
 */
public abstract class HttpServlet implements Servlet {

	@Override
	public void init() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void service(Request request, Response response) throws IOException, ServletException {
		if (request.getMethod().equalsIgnoreCase(RequestMethod.GET)) {
			doGet(request, response);
		} else if (request.getMethod().equalsIgnoreCase(RequestMethod.POST)) {
			doPost(request, response);
		}
	}

	public void doGet(Request request, Response response) throws IOException, ServletException {

	}

	public void doPost(Request request, Response response) throws IOException, ServletException {

	}

}
