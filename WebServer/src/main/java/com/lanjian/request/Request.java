package com.lanjian.request;

import static com.lanjian.constant.CharConstant.BLANK;
import static com.lanjian.constant.CharConstant.CRLF;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public String getRequestInfo() {
		return requestInfo;
	}

	public int getLen() {
		return len;
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	// 请求方法
	private String method;
	// 请求地址
	private String url;
	// 请求参数
	private String parameters;
	private Map<String, List<String>> parameterMap;

	public Request(Socket client) throws IOException {
		// 接收客户端请求
		in = client.getInputStream();
		parameterMap = new HashMap<String, List<String>>();
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
			// url中没有参数
			url = urlStr;
			parameters = "";
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
			// GET方法不需要单独处理
		} else {
			LogUtil.error("不支持的请求方法");
		}
//		System.out.println(method);
//		System.out.println(url);
//		System.out.println(parameters);
		// 将parameters转换成map格式
		convertMap();
		LogUtil.info("请求解析完成");
	}

	/**
	 * @explain 将字符串格式的参数转化成map格式，如：name=name1&name=name2 --> {name=[name1,name2]}
	 */
	private void convertMap() {
		// ["name=name1", "name=name2"]
		String[] paramsArr = parameters.split("&");
		for (String params : paramsArr) {
			// ["name", "name1"]
			String[] param = params.split("=");
			String paramKey = param[0];
			String paramValue = "";
			if (param.length > 1) {
				// url后的参数如果存在中文需要解码
				paramValue = decode(param[1], "UTF-8");
			}
			if (parameterMap.containsKey(paramKey)) {
				parameterMap.get(paramKey).add(paramValue);
			} else {
				List<String> paramValues = new ArrayList<String>();
				paramValues.add(paramValue);
				parameterMap.put(paramKey, paramValues);
			}
		}
	}

	/**
	 * @explain 根据key获取参数值
	 */
	public String[] getParameterValues(String key) {
		List<String> parameterList;
		if (parameterMap.containsKey(key)) {
			parameterList = parameterMap.get(key);
		} else {
			return new String[] {};
		}
		return parameterList.toArray(new String[0]);
	}

	/**
	 * @explain 根据key获取一个参数值
	 */
	public String getParameterValue(String key) {
		List<String> parameterList;
		if (parameterMap.containsKey(key)) {
			parameterList = parameterMap.get(key);
		} else {
			return "";
		}
		return parameterList.get(0);
	}

	/**
	 * @explain 解码
	 */
	public String decode(String str, String charSet) {
		try {
			return URLDecoder.decode(str, charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LogUtil.error("解码失败");
		}
		return "";
	}
}
