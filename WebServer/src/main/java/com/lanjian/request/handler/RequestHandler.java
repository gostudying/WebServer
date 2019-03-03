package com.lanjian.request.handler;

import java.io.IOException;
import java.util.List;

import com.lanjian.context.ServletContext;
import com.lanjian.exception.ServerErrorException;
import com.lanjian.exception.ServletNotFoundException;
import com.lanjian.exception.base.ServletException;
import com.lanjian.exception.handler.ExceptionHandler;
import com.lanjian.filter.Filter;
import com.lanjian.filter.FilterChain;
import com.lanjian.request.ServletRequest;
import com.lanjian.response.ServletResponse;
import com.lanjian.servlet.http.HttpServlet;

/**
 * @explain 处理request请求
 * @author lanjian
 * @date 2019年3月2日
 */
public class RequestHandler implements Runnable, FilterChain {
	private ServletResponse response;
	private ServletRequest request;
	private HttpServlet servlet;
	private ExceptionHandler exceptionHandler;
	private List<Filter> filters;
	private ServletContext servletContext;
	private int filterIndex = 0;

	public RequestHandler(ServletRequest request, ServletResponse response, ServletContext servletContext)
			throws ServletNotFoundException {
		this.request = request;
		this.response = response;
		this.servletContext = servletContext;
		this.servlet = servletContext.getServlet(request.getUrl());
		this.filters = servletContext.getFilters(request.getUrl());
	}

	@Override
	public void run() {
		// 如果没有filter，则直接执行servlet
		if (filters.isEmpty()) {
			service();
		} else {
			// 先执行filter
			doFilter(request, response);
		}

	}

	/**
	 * @explain 递归执行
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response) {
		if (filterIndex < filters.size()) {
			filters.get(filterIndex++).doFilter(request, response, this);
		} else {
			service();
		}
	}

	void service() {
		try {
			servlet.service(request, response);
		} catch (IOException e) {
			exceptionHandler.handle(new ServerErrorException(), response);
		} catch (ServletException e) {
			exceptionHandler.handle(e, response);
		}
	}
}
