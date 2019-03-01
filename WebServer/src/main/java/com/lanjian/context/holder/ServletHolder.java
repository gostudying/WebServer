package com.lanjian.context.holder;

import com.lanjian.servlet.Servlet;

/**
 * @explain 根据类名找到一个类
 * @author lanjian
 * @date 2019年3月1日
 */
public class ServletHolder {
	private Servlet servlet;
	private String servletClass;

	public ServletHolder(String servletClass) {
		this.servletClass = servletClass;
	}
}
