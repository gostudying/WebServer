package com.lanjian.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.lanjian.dispatcher.Dispatcher;
import com.lanjian.utils.CloseUtil;
import com.lanjian.utils.LogUtil;

/**
 * @explain 服务器类
 * @author lanjian
 * @date 2019年2月28日
 */
public class Server {
	private ServerSocket serverSocket;
	private Dispatcher dispatcher;
	private boolean isStop = false;

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
		dispatcher = new Dispatcher();
		while (!isStop) {
			try {
				LogUtil.info("正在等待客户端连接......");
				client = serverSocket.accept();
				// 客户端连接后，创建新线程去处理
				dispatcher.doDispatch(client);
			} catch (IOException e) {
				e.printStackTrace();
				LogUtil.error("客户端--" + client.getRemoteSocketAddress() + "--连接失败");
			}
		}
	}

	/**
	 * @explain 关闭服务器
	 */
	public void close() {
		// 关闭主循环
		this.isStop = true;
		// 关闭服务器
		CloseUtil.close(serverSocket);
	}

}
