package com.lanjian.servlet.http;

import java.io.IOException;

import com.lanjian.constant.RequestMethod;
import com.lanjian.exception.base.ServletException;
import com.lanjian.request.ServletRequest;
import com.lanjian.response.ServletResponse;
import com.lanjian.servlet.Servlet;

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
	public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		if (request.getMethod().equalsIgnoreCase(RequestMethod.GET)) {
			doGet(request, response);
		} else if (request.getMethod().equalsIgnoreCase(RequestMethod.POST)) {
			doPost(request, response);
		}
	}

	public void doGet(ServletRequest request, ServletResponse response) throws IOException, ServletException {

	}

	public void doPost(ServletRequest request, ServletResponse response) throws IOException, ServletException {

	}

}
