package com.lanjian.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lanjian.exception.ServletNotFoundException;
import com.lanjian.exception.handler.ExceptionHandler;
import com.lanjian.request.ServletRequest;
import com.lanjian.request.handler.RequestHandler;
import com.lanjian.response.ServletResponse;

/**
 * @explain 分发器，为每个客户端找到对应的requestHandler去处理
 * @author lanjian
 * @date 2019年3月2日
 */
public class Dispatcher {
	private ExecutorService pool;
	private ExceptionHandler exceptionHandler;

	public Dispatcher() {
		pool = Executors.newFixedThreadPool(10);
		exceptionHandler = new ExceptionHandler();
	}

	public void doDispatch(Socket client) throws IOException {
		ServletRequest request = new ServletRequest(client);
		ServletResponse response = new ServletResponse(client);
		try {
			RequestHandler requestHandler = new RequestHandler(request, response);
			pool.execute(requestHandler);
		} catch (ServletNotFoundException e) {
			exceptionHandler.handle(e, response);
		}
	}
}
