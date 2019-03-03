package com.lanjian.servlet;

import java.io.IOException;

import com.lanjian.context.ServletContext;
import com.lanjian.exception.base.ServletException;
import com.lanjian.request.ServletRequest;
import com.lanjian.response.ServletResponse;

/**
 * @explain 为servlet接口提供一些默认方法
 * @author lanjian
 * @date 2019年3月2日
 */
public abstract class GenericServlet implements Servlet, ServletConfig {
	private ServletConfig config;

	public GenericServlet() {
	}

	public void destroy() {
	}

	public ServletConfig getServletConfig() {
		return config;
	}

	public ServletContext getServletContext() {
		ServletConfig sc = getServletConfig();
		if (sc == null) {
			return null;
		}
		return sc.getServletContext();
	}

	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		this.init();
	}

	public void init() throws ServletException {
	}

	public abstract void service(ServletRequest req, ServletResponse res) throws ServletException, IOException;

	public String getServletName() {
		ServletConfig sc = getServletConfig();
		if (sc == null) {
			return null;
		}
		return sc.getServletName();
	}
}
