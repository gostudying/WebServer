package com.lanjian.dispatcher;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lanjian.context.ServletContext;
import com.lanjian.context.WebApplication;
import com.lanjian.dispatcher.handler.RequestHandler;
import com.lanjian.exception.ServletNotFoundException;
import com.lanjian.exception.handler.ExceptionHandler;
import com.lanjian.request.Request;
import com.lanjian.response.Response;
import com.lanjian.servlet.HttpServlet;

/**
 * @explain 多任务分发器
 * @author lanjian
 * @date 2019年3月2日
 */
public class RequestDispatcher {
	private ExecutorService pool;
	private ServletContext servletContext;
	private ExceptionHandler exceptionHandler;

	public RequestDispatcher() {
		servletContext = WebApplication.getServletContext();
		pool = Executors.newFixedThreadPool(10);
		exceptionHandler = new ExceptionHandler();
	}

	public void doDispatch(Socket client) throws IOException {
		Request request = new Request(client);
		Response response = new Response(client);
		try {
			HttpServlet servlet = servletContext.getServlet(request.getUrl());
			RequestHandler requestHandler = new RequestHandler(request, response, servlet);
			pool.execute(requestHandler);
		} catch (ServletNotFoundException e) {
			exceptionHandler.handle(e, response);
		}
	}
}
