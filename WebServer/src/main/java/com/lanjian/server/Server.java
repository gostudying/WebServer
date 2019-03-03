package com.lanjian.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.lanjian.utils.CloseUtil;
import com.lanjian.utils.LogUtil;

/**
 * @explain 服务器类
 * @author lanjian
 * @date 2019年2月28日
 */
public class Server {
	private ServerSocket serverSocket;
	private Acceptor acceptor;
	private volatile boolean isRunning = true;

	/**
	 * @explain 开启服务器
	 */
	public void start(int port) {
		try {
			serverSocket = new ServerSocket(port);
			LogUtil.info("服务器启动成功");
			initAcceptor();
		} catch (IOException e) {
			CloseUtil.close(serverSocket);
			e.printStackTrace();
			LogUtil.error("服务器启动失败");
		}
	}

	/**
	 * @explain 新建一个线程去处理连接请求
	 */
	private void initAcceptor() {
		acceptor = new Acceptor(this);
		Thread t = new Thread(acceptor, "thread-accept");
		// 设置为后台线程
		t.setDaemon(true);
		t.start();
	}

	public Socket accept() throws IOException {
		return serverSocket.accept();
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * @explain 关闭服务器
	 */
	public void close() {
		// 关闭主循环
		this.isRunning = false;
		// 关闭服务器
		CloseUtil.close(serverSocket);
		// 关闭接收器
		acceptor.close();
	}

}
