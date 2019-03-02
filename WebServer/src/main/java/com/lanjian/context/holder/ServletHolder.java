package com.lanjian.context.holder;

import com.lanjian.servlet.HttpServlet;

/**
 * @explain 将类名和类实例捆绑在一起
 * @author lanjian
 * @date 2019年3月1日
 */
public class ServletHolder {
	private HttpServlet servlet;
	private String servletClass;

	public ServletHolder(String servletClass) {
		this.servletClass = servletClass;
	}

	public HttpServlet getServlet() {
		return servlet;
	}

	public void setServlet(HttpServlet servlet) {
		this.servlet = servlet;
	}

	public String getServletClass() {
		return servletClass;
	}

	public void setServletClass(String servletClass) {
		this.servletClass = servletClass;
	}

}
