package com.lanjian.servlet.impl;

import java.io.IOException;

import com.lanjian.constant.HttpStatus;
import com.lanjian.cookie.Cookie;
import com.lanjian.exception.ServerErrorException;
import com.lanjian.exception.base.ServletException;
import com.lanjian.request.ServletRequest;
import com.lanjian.response.ServletResponse;
import com.lanjian.servlet.http.HttpServlet;
import com.lanjian.utils.LogUtil;

public class IndexServlet extends HttpServlet {

	@Override
	public void init() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void doGet(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		try {
			LogUtil.info("正在向浏览器返回数据......");
			// 响应内容
			response.print("<html>");
			response.print("<head>");
			response.print("<title>");
			response.print("服务器响应成功");
			response.print("</title>");
			response.print("</head>");
			response.print("<body>");
			response.print("你好index");
			response.print("</body>");
			response.print("</html>");
			response.sendRedirect("http://www.baidu.com");
			response.addCookie(new Cookie("name", "lanjian"));
			response.flush(HttpStatus.OK);
		} catch (Exception e) {
			throw new ServerErrorException();
		}
	}

	@Override
	public void doPost(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		
	}

}
