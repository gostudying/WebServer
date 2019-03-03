package com.lanjian.context;

/**
 * @explain static变量让servletContext在项目启动时就被初始化
 * @author lanjian
 * @date 2019年3月1日
 */
public class WebApplication {
	// 这是一种单例模式，程序启动后，内存中只有一个ServletContext
	private static ServletContext servletContext;

	static {
		servletContext = new ServletContext();
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}
}
