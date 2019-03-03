package com.lanjian.server;

import java.io.IOException;
import java.net.Socket;

import com.lanjian.utils.CloseUtil;
import com.lanjian.utils.LogUtil;

public class Acceptor implements Runnable {
	private Server server;
	private Socket client;
	private Dispatcher dispatcher;

	public Acceptor(Server server) {
		this.server = server;
		this.dispatcher = new Dispatcher();
	}

	@Override
	public void run() {
		while (server.isRunning()) {
			LogUtil.info("正在等待客户端连接......");
			try {
				client = server.accept();
				// 客户端连接后，将客户端交给分发器处理
				dispatcher.doDispatch(client);
			} catch (IOException e) {
				e.printStackTrace();
				LogUtil.error("客户端--" + client.getRemoteSocketAddress() + "--连接失败");
			}
		}
	}

	public void close() {
		CloseUtil.close(client);
	}
}
