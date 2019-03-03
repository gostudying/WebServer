package com.lanjian.servlet;

import com.lanjian.context.ServletContext;

public interface ServletConfig {
	public String getServletName();

	public ServletContext getServletContext();
}
