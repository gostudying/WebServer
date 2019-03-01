package com.lanjian.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import com.lanjian.context.ServletContext;
import com.lanjian.context.WebApplication;
import com.lanjian.request.Request;
import com.lanjian.response.Response;
import com.lanjian.servlet.Servlet;
import com.lanjian.utils.CloseUtil;
import com.lanjian.utils.LogUtil;

/**
 * @explain 服务器类
 * @author lanjian
 * @date 2019年2月28日
 */
public class Server {
	private ServerSocket serverSocket;

	/**
	 * @explain 开启服务器
	 */
	public void start(int port) {
		try {
			serverSocket = new ServerSocket(port);
			LogUtil.info("服务器启动成功");
			receive();
		} catch (IOException e) {
			CloseUtil.close(serverSocket);
			e.printStackTrace();
			LogUtil.error("服务器启动失败");
		}
	}

	/**
	 * @explain 处理连接请求
	 */
	private void receive() {
		Socket client = null;
		LogUtil.info("正在等待客户端连接......");
		try {
			client = serverSocket.accept();
			Request request = new Request(client);
			Response response = new Response(client);
			System.out.println(request.getMethod());
			System.out.println(request.getUrl());
			System.out.println(Arrays.toString(request.getParameterValues("name")));
			// 得到上下文对象
			ServletContext context = WebApplication.getServletContext();
			Servlet servlet = context.getServlet(request.getUrl());
			servlet.service(request, response);

		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.error("客户端--" + client.getRemoteSocketAddress() + "--连接失败");
		} finally {
			CloseUtil.close(client);
		}
	}

	/**
	 * @explain 关闭服务器
	 */
	public void close() {
		CloseUtil.close(serverSocket);
	}

}
