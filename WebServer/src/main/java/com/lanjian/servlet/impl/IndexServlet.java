package com.lanjian.servlet.impl;

import java.io.IOException;

import com.lanjian.enums.HttpStatus;
import com.lanjian.request.Request;
import com.lanjian.response.Response;
import com.lanjian.servlet.Servlet;

public class IndexServlet implements Servlet {

	@Override
	public void init() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void service(Request request, Response response) throws IOException {
		try {
			// 响应内容
			response.print("<html>");
			response.print("<head>");
			response.print("<title>");
			response.print("服务器响应成功");
			response.print("</title>");
			response.print("</head>");
			response.print("<body>");
			response.print("你好");
			response.print("</body>");
			response.print("</html>");
			response.pushToClient(HttpStatus.OK);
		} catch (Exception e) {
			response.pushToClient(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
