package com.lanjian.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.lanjian.enums.HttpStatus;
import com.lanjian.request.Request;
import com.lanjian.response.Response;
import com.lanjian.utils.CloseUtil;
import com.lanjian.utils.LogUtil;

/**
 * @explain 服务器类
 * @author lanjian
 * @date 2019年2月28日
 */
public class Server {
	private ServerSocket serverSocket;
	private InputStream in;
	private BufferedWriter out;

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
		try {
			LogUtil.info("正在等待客户端连接......");
			client = serverSocket.accept();
			Request request = new Request(client);
			Response response = new Response(client);
			try {
				// 响应内容
				response.print("<html>");
				response.print("<head>");
				response.print("<title>");
				response.print("服务器响应成功");
				response.print("</title>");
				response.print("</head>");
				response.print("<body>");
				response.print("hello");
				response.print("</body>");
				response.print("</html>");
				response.pushToClient(HttpStatus.OK);
			} catch (Exception e) {
				response.pushToClient(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.error("客户端--" + client.getRemoteSocketAddress() + "--连接失败");
		} finally {
			CloseUtil.close(client, in, out);
		}
	}

	/**
	 * @explain 关闭服务器
	 */
	public void close() {

	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start(8080);
	}

}
