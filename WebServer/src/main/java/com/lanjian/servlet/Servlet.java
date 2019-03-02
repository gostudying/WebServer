package com.lanjian.servlet;

import java.io.IOException;

import com.lanjian.exception.base.ServletException;
import com.lanjian.request.Request;
import com.lanjian.response.Response;

/**
 * @explain Servlet接口，具体的Servlet用于处理不同的客户端请求，并返回相应内容
 * @author lanjian
 * @date 2019年3月1日
 */
public interface Servlet {
	void init();

	void destroy();

	void service(Request request, Response response) throws IOException, ServletException;
}
