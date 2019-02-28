package com.lanjian.server;

import static com.lanjian.constant.CharConstant.BLANK;
import static com.lanjian.constant.CharConstant.CRLF;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import com.lanjian.enums.HttpStatus;
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
			client = serverSocket.accept();
			LogUtil.info("客户端--" + client.getRemoteSocketAddress() + "--连接成功");
			// 接收客户端请求
			in = client.getInputStream();
			byte[] data = new byte[20480];
			int len = in.read(data);
			String requestInfo = new String(data, 0, len).trim();
			System.out.println(requestInfo);

			// 响应内容
			StringBuilder content = new StringBuilder();
			content.append("<html>");
			content.append("<head>");
			content.append("<title>");
			content.append("服务器响应成功");
			content.append("</title>");
			content.append("</head>");
			content.append("<body>");
			content.append("hello");
			content.append("</body>");
			content.append("</html>");

			// 响应：响应头+响应内容
			StringBuilder responseInfo = new StringBuilder();
			responseInfo.append("HTTP/1.1").append(BLANK).append(HttpStatus.OK).append(BLANK).append("OK").append(CRLF);
			responseInfo.append("Date:").append(new Date()).append(CRLF);
			responseInfo.append("Server:WebServer/1.0;charset=GBK").append(CRLF);
			responseInfo.append("Content-type:text/html").append(CRLF);
			// 字节长度，不是字符长度
			responseInfo.append("Content-length:").append(content.toString().getBytes().length).append(CRLF);
			responseInfo.append(CRLF);
			responseInfo.append(content);

			// 写出响应
			out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			out.write(responseInfo.toString());
			out.flush();
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
