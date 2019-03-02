package com.lanjian.response;

import static com.lanjian.constant.CharConstant.BLANK;
import static com.lanjian.constant.CharConstant.CRLF;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.lanjian.enums.HttpStatus;
import com.lanjian.utils.CloseUtil;

/**
 * @explain 封装响应信息
 * @author lanjian
 * @date 2019年2月28日
 */
public class Response {
	// 输出缓冲流
	private BufferedWriter out;
	// 正文
	private StringBuilder content;
	// 正文字节数
	private int len;
	// 响应头
	private StringBuilder headInfo;

	private Response() {
		content = new StringBuilder();
		len = 0;
		headInfo = new StringBuilder();
	}

	public Response(Socket client) throws IOException {
		this();
		out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	}

	/**
	 * @explain 根据状态码构建响应头信息
	 */
	private void createHeadInfo(HttpStatus code) {
		headInfo.append("HTTP/1.1").append(BLANK).append(code.getCode()).append(BLANK);
		switch (code) {
		case OK:
			headInfo.append(HttpStatus.OK.getMsg());
			break;
		case NOT_FOUND:
			headInfo.append(HttpStatus.NOT_FOUND.getMsg());
			break;
		case INTERNAL_SERVER_ERROR:
			headInfo.append(HttpStatus.INTERNAL_SERVER_ERROR.getMsg());
			break;
		case BAD_REQUEST:
			headInfo.append(HttpStatus.BAD_REQUEST.getMsg());
			break;
		default:
			break;
		}
		headInfo.append(CRLF);
		headInfo.append("Date:").append(new Date()).append(CRLF);
		headInfo.append("Server:WebServer/1.0;charset=GBK").append(CRLF);
		headInfo.append("Content-type:text/html").append(CRLF);
		// 字节长度，不是字符长度
		headInfo.append("Content-length:").append(len).append(CRLF);
		headInfo.append(CRLF);
	}

	public Response print(String info) {
		content.append(info);
		len += info.getBytes().length;
		return this;
	}

	public Response println(String info) {
		content.append(info).append(CRLF);
		len += info.getBytes().length + CRLF.length();
		return this;
	}

	/**
	 * @throws IOException
	 * @explain 推送响应到客户端
	 */
	public void pushToClient(HttpStatus code) throws IOException {
		try {
			// 创建头信息
			createHeadInfo(code);
			// 添加响应头
			out.append(headInfo.toString());
			// 添加响应内容
			if (!StringUtils.isBlank(content)) {
				out.append(content.toString());
			}
			out.flush();
		} finally {
			CloseUtil.close(out);
		}
	}

}
