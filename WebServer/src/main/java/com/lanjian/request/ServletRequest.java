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
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.lanjian.context.ServletContext;
import com.lanjian.context.WebApplication;
import com.lanjian.cookie.Cookie;
import com.lanjian.request.dispatcher.RequestDispatcher;
import com.lanjian.request.handler.RequestHandler;
import com.lanjian.session.HttpSession;
import com.lanjian.utils.LogUtil;

/**
 * @explain 封装请求信息
 * @author lanjian
 * @date 2019年2月28日
 */
public class ServletRequest {
	// 输入流
	private InputStream in;
	// 请求消息
	private String requestInfo;
	// 请求消息长度
	private int contentLength;
	// 请求方法
	private String method;
	// 请求地址
	private String requestURI;
	// 请求参数
	private String parameters;
	private Map<String, List<String>> parameterMap;
	private List<Cookie> cookies;
	// request域
	private Map<String, Object> attributes;
	private HttpSession session;
	private ServletContext context;
	private RequestHandler requestHandler;

	public ServletRequest() {
		attributes = new ConcurrentHashMap<String, Object>();
		parameterMap = new HashMap<String, List<String>>();
		cookies = new ArrayList<Cookie>();
		context = WebApplication.getServletContext();
	}

	public ServletRequest(Socket client) throws IOException {
		this();
		// 接收客户端请求
		in = client.getInputStream();
		// 一次性将request请求读取进来
		byte[] data = new byte[20480];
		int len = in.read(data);
		requestInfo = new String(data, 0, len).trim();
		LogUtil.info("客户端连接成功===" + client.getRemoteSocketAddress());
		// 解析请求
		parseRequestInfo();
	}

	public HttpSession getSession() {
		if (session != null) {
			// 如果当前请求已经存在对应的Session，则直接返回
			return session;
		}
		// 如果当前请求不存在对应的Session，则找到请求携带的cookie中JSESSIONID
		// 用JSESSIONID在ServletContext中管理的所有session中找，如果找到则返回
		// 如果还找不到，则为该用户创建一个session，并将sessionId写到浏览器cookie中
		for (Cookie cookie : cookies) {
			if (cookie.getKey().equals("JSESSIONID")) {
				HttpSession curSession = context.getSession(cookie.getValue());
				if (curSession != null) {
					session = curSession;
					return session;
				}
			}
		}
		session = context.createSession(requestHandler.getResponse());
		return session;
	}

	public RequestDispatcher getRequestDispatcher() {
		return new RequestDispatcher();
	}

	public ServletContext getServletContext() {
		return this.context;
	}

	/**
	 * @explain 解析请求
	 */
	private void parseRequestInfo() {
		LogUtil.info("正在解析请求......");
		// 第一行数据
		// GET /index?name=name1&name=name2 HTTP/1.1
		String firstLine = requestInfo.substring(0, requestInfo.indexOf(CRLF));
		this.method = firstLine.substring(0, firstLine.indexOf(BLANK));
		String urlStr = firstLine.substring(firstLine.indexOf(BLANK) + 1, firstLine.lastIndexOf(BLANK));
		if (urlStr.contains("?")) {
			// url中有参数
			// ?需要转义
			String[] urlArr = urlStr.split("\\?");
			requestURI = urlArr[0];
			if (urlArr.length > 1) {
				parameters = urlArr[1];
			}
		} else {
			// url中没有参数
			requestURI = urlStr;
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
		// 将parameters转换成map格式
		convertMap();
		// 解析cookie
		parseCookies();
		LogUtil.info("请求解析完成");
	}

	// Cookie: _za=882fe640-db90-4aee-9928-0699aa5ff475;
	// _zap=34c2f4bb-698e-46d7-b036-00fab7e1d9c0;
	// JSESSIONID=CA7F829A6ED0ABA6BC3906BEF2992A54
	private void parseCookies() {
		if (requestInfo.contains("Cookie")) {
			String cookieTemp = requestInfo.substring(requestInfo.indexOf("Cookie")).trim();
			int indexOf = cookieTemp.indexOf(CRLF);
			String s1;
			if (indexOf > -1) {
				s1 = cookieTemp.substring(0, indexOf);
			} else {
				// cookie在请求最后一行
				s1 = cookieTemp;
			}
			String cookieStr = s1.split(":")[1].trim();
			String[] cookieArr = cookieStr.split(";");
			for (String cookie : cookieArr) {
				String[] cookieKV = cookie.split("=");
				String key = cookieKV[0];
				String value = "";
				if (cookieKV.length > 1) {
					value = cookieKV[1];
				}
				cookies.add(new Cookie(key, value));
			}
		}
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
		List<String> parameterList = parameterMap.get(key);
		if (parameterList == null) {
			return new String[] {};
		} else {
			return parameterList.toArray(new String[0]);
		}
	}

	/**
	 * @explain 根据key获取一个参数值
	 */
	public String getParameter(String key) {
		List<String> parameterList = parameterMap.get(key);
		if (parameterList == null) {
			return null;
		}
		return parameterList.get(0);
	}

	/**
	 * @explain 解码
	 */
	private String decode(String str, String charSet) {
		try {
			return URLDecoder.decode(str, charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LogUtil.error("解码失败");
		}
		return "";
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public int getContentLength() {
		return contentLength;
	}

	public String getMethod() {
		return method;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public Object getAttributes(String key) {
		return attributes.get(key);
	}

	public void setAttributes(String key, Object value) {
		this.attributes.put(key, value);
	}

	public void removeAttributes(String key) {
		if (this.attributes.containsKey(key)) {
			this.attributes.remove(key);
		}
	}

	public Cookie[] getCookies(String key) {
		return cookies.toArray(new Cookie[1]);
	}

}
