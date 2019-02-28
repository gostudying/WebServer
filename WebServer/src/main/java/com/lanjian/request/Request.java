package com.lanjian.request;

import static com.lanjian.constant.CharConstant.BLANK;
import static com.lanjian.constant.CharConstant.CRLF;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;

import com.lanjian.utils.LogUtil;

/**
 * @explain 封装请求信息
 * @author lanjian
 * @date 2019年2月28日
 */
public class Request {
	// 输入流
	private InputStream in;
	// 请求消息
	private String requestInfo;
	// 请求消息长度
	private int len;
	// 请求方法
	private String method;
	// 请求地址
	private String url;
	// 请求参数
	private String parameters;

	public Request(Socket client) throws IOException {
		// 接收客户端请求
		in = client.getInputStream();
		byte[] data = new byte[20480];
		int len = in.read(data);
		requestInfo = new String(data, 0, len).trim();
		LogUtil.info("客户端连接成功===" + client.getRemoteSocketAddress());
		// 解析请求
		parseRequestInfo();
	}

	/**
	 * @explain 解析请求
	 */
	private void parseRequestInfo() {
		LogUtil.info("正在解析请求......");

		// 第一行数据
		// GET /index?name=name1&name=name2 HTTP/1.1
		String firstLine = requestInfo.substring(0, requestInfo.indexOf(CRLF));
		method = firstLine.substring(0, firstLine.indexOf(BLANK));
		String urlStr = firstLine.substring(firstLine.indexOf(BLANK) + 1, firstLine.lastIndexOf(BLANK));
		if (urlStr.contains("?")) {
			// url中有参数
			// ?需要转义
			String[] urlArr = urlStr.split("\\?");
			url = urlArr[0];
			if (urlArr.length > 1) {
				parameters = urlArr[1];
			}
		} else {
			url = urlStr;
		}
		if (method.equalsIgnoreCase("POST")) {
			// 如果是POST请求方式，则请求体中也有可能携带参数
			String requestBody = requestInfo.substring(requestInfo.lastIndexOf(CRLF)).trim();
			if (!StringUtils.isBlank(requestBody)) {
				if (StringUtils.isBlank(parameters)) {
					// 请求参数只在请求体
					parameters = requestBody;
				} else {
					// 请求参数在url和请求体两个中
					parameters += "&" + requestBody;
				}
			}
		} else if (method.equalsIgnoreCase("GET")) {

		} else {
			LogUtil.error("不支持的请求方法");
		}
		System.out.println(method);
		System.out.println(url);
		System.out.println(parameters);
		LogUtil.info("请求解析完成");
	}
}
