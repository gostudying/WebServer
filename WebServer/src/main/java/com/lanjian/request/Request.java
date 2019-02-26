package com.lanjian.request;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Request {
	private static final String CRLF = "\r\n";
	private static final String BLANK = " ";
	// 请求方式
	private String method;
	// 请求url
	private String url;
	// 请求参数
	private Map<String, List<String>> paramMap;
	// 客户端输入流
	private InputStream inputStream;
	// 客户端请求信息
	private String requestInfo;

	public Request() {
		method = "";
		url = "";
		requestInfo = "";
		paramMap = new HashMap<String, List<String>>();
	}

	public Request(InputStream inputStream) throws IOException {
		this();
		this.inputStream = inputStream;
		byte[] data = new byte[20480];
		// 读取请求信息
		int len = inputStream.read(data);
		requestInfo = new String(data, 0, len);
		// 解析请求信息
		parseRequestInfo();
	}

	private void parseRequestInfo() {
		if (StringUtils.isBlank(requestInfo)) {
			return;
		}
		// 请求参数
		String paramString = "";
		String firstLine = requestInfo.substring(0, requestInfo.indexOf(CRLF));
		// GET /s?name=name&&pwd=pwd HTTP/1.1
		int index = firstLine.indexOf(BLANK);
		this.method = firstLine.substring(0, index);
		String urlStr = firstLine.substring(index + 1, firstLine.lastIndexOf(BLANK));
		if (this.method.equalsIgnoreCase("POST")) {
			// post方式url中不带参数
			this.url = urlStr;
			// post方法 参数在请求体中
			paramString = requestInfo.substring(requestInfo.lastIndexOf(CRLF)).trim();
		} else if (this.method.equalsIgnoreCase("GET")) {
			// get方式url中带参数
			if (urlStr.contains("?")) {
				// ?需要转义
				String[] urlArr = urlStr.split("\\?");
				this.url = urlArr[0];
				paramString = urlArr[1];
			} else {
				this.url = urlStr;
			}
		} else {
			throw new RuntimeException("不支持的请求方法");
		}

		// 将请求参数封装到请求map中
		if (!paramString.equals("")) {
			parseParams(paramString);
		}
	}

	/**
	 * 将请求参数封装到请求map中
	 * 
	 * @param paramString uname=uname1&uname=uname2&pwd=pwd1&pwd2=pwd2
	 */
	private void parseParams(String paramString) {
		// ["uname=uname1","uname=uname2","pwd=pwd1","pwd2=pwd2"]
		String[] paramArr = paramString.split("&");
		for (int i = 0; i < paramArr.length; ++i) {
			// ["uname","uname1"]
			String[] param = paramArr[i].split("=");
			String key = param[0];
			String value = null;
			// value可能存在或不存在
			if (param.length == 2) {
				value = param[1];
			}
			if (paramMap.containsKey(key)) {
				paramMap.get(key).add(value);
			} else {
				ArrayList<String> valueList = new ArrayList<String>();
				valueList.add(value);
				paramMap.put(key, valueList);
			}
		}
	}

	public String[] getParameters(String name) {
		if (paramMap.containsKey(name)) {
			List<String> values = paramMap.get(name);
			return values.toArray(new String[0]);
		} else {
			return null;
		}
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getParameter(String name) {
		String[] values = getParameters(name);
		if (values == null) {
			return null;
		}
		return values[0];
	}
}
