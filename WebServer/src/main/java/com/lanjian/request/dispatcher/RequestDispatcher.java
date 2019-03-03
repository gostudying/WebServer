package com.lanjian.request.dispatcher;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lanjian.context.ServletContext;
import com.lanjian.context.WebApplication;
import com.lanjian.exception.ServletNotFoundException;
import com.lanjian.exception.handler.ExceptionHandler;
import com.lanjian.request.ServletRequest;
import com.lanjian.request.handler.RequestHandler;
import com.lanjian.response.ServletResponse;

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
		ServletRequest request = new ServletRequest(client);
		ServletResponse response = new ServletResponse(client);
		try {
			RequestHandler requestHandler = new RequestHandler(request, response, servletContext);
			pool.execute(requestHandler);
		} catch (ServletNotFoundException e) {
			exceptionHandler.handle(e, response);
		}
	}
}
