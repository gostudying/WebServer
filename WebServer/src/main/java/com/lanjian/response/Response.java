package com.lanjian.response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

public class Response {
	private static final String CRLF = "\r\n";
	private static final String BLANK = " ";
	// 响应头
	private StringBuilder headInfo;
	// 响应体
	private StringBuilder content;
	// 响应体长度，字节数
	private int len;
	// 客户端输出流
	private BufferedWriter bufferedWriter;

	public Response(OutputStream os) {
		len = 0;
		headInfo = new StringBuilder();
		content = new StringBuilder();
		bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
	}

	/**
	 * 创建响应体
	 * 
	 * @param info 响应体内容
	 * @return
	 */
	public Response println(String info) {
		content.append(info).append(CRLF);
		len += info.getBytes().length;
		len += CRLF.getBytes().length;
		return this;
	}

	/**
	 * 创建响应头
	 * 
	 * @param code 状态码
	 */
	private void createHeadInfo(String code) {
		headInfo.append("HTTP/1.1").append(BLANK);
		headInfo.append(code).append(BLANK);
		switch (code) {
		case "200":
			headInfo.append("OK");
			break;
		case "404":
			headInfo.append("NOT FOUND");
			break;
		}
		headInfo.append(CRLF);
		headInfo.append("Server:WebServer 1.0").append(CRLF);
		headInfo.append("Date:").append(new Date()).append(CRLF);
		headInfo.append("Content-type:text/html;charset=UTF8").append(CRLF);
		headInfo.append("Content-Length:").append(len).append(CRLF);
		headInfo.append(CRLF);
	}

	public void pushToClient(String code) throws IOException {
		// 创建响应头
		createHeadInfo(code);
		// 将响应头加入输出缓冲区
		bufferedWriter.append(headInfo.toString());
		// 将响应体加入输出缓冲区
		bufferedWriter.append(content.toString());
		bufferedWriter.flush();
	}

	public void close() {

		try {
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
