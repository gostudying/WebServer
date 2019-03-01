package com.lanjian.context;

/**
 * @explain 让servletContext在项目启动时就被初始化
 * @author lanjian
 * @date 2019年3月1日
 */
public class WebApplication {
	private static ServletContext servletContext;

	static {
		servletContext = new ServletContext();
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}
}
