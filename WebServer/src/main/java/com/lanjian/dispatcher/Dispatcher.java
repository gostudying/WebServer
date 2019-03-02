package com.lanjian.dispatcher;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lanjian.context.ServletContext;
import com.lanjian.context.WebApplication;
import com.lanjian.dispatcher.handler.RequestHandler;
import com.lanjian.request.Request;
import com.lanjian.response.Response;
import com.lanjian.servlet.Servlet;
import com.lanjian.utils.LogUtil;

/**
 * @explain 多任务分发器
 * @author lanjian
 * @date 2019年3月2日
 */
public class Dispatcher {
	private ExecutorService pool;
	private ServletContext servletContext;

	public Dispatcher() {
		servletContext = WebApplication.getServletContext();
		pool = Executors.newFixedThreadPool(10);
	}

	public void doDispatch(Socket client) throws IOException {
		Request request = new Request(client);
		Response response = new Response(client);
		Servlet servlet = servletContext.getServlet(request.getUrl());
		if (servlet == null) {
			LogUtil.info("找不到对应servlet");
		} else {
			RequestHandler requestHandler = new RequestHandler(request, response, servlet);
			pool.execute(requestHandler);
		}
	}
}
