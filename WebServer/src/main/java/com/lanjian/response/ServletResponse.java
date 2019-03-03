package com.lanjian.response;

import static com.lanjian.constant.CharConstant.BLANK;
import static com.lanjian.constant.CharConstant.CRLF;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.lanjian.constant.HttpStatus;
import com.lanjian.utils.CloseUtil;
import com.lanjian.utils.LogUtil;

/**
 * @explain 封装响应信息
 * @author lanjian
 * @date 2019年2月28日
 */
public class ServletResponse {
	// 输出缓冲流
	private BufferedWriter out;
	// 正文
	private StringBuilder content;
	// 正文字节数
	private int len;
	// 响应头
	private StringBuilder headInfo;
	private Map<String, String> cookies;
	private StringBuilder cookieStr;

//	public String getCharacterEncoding();
//
//	public void setCharacterEncoding(String charset);
//
//	public String getContentType();
//
//	public void setContentType(String type);
//
//	public void setContentLength(int len);
//
//	public PrintWriter getWriter() throws IOException;
//
//	public void flushBuffer() throws IOException;
	
	private ServletResponse() {
		content = new StringBuilder();
		len = 0;
		headInfo = new StringBuilder();
		cookies = new HashMap<>();
		cookieStr = new StringBuilder();
	}

	public ServletResponse(Socket client) throws IOException {
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
		convertCookie();
		if (!StringUtils.isBlank(cookieStr)) {
			headInfo.append("Set-Cookie:" + cookieStr.toString()).append(CRLF);
		}
		headInfo.append(CRLF);
	}

	public ServletResponse print(String info) {
		content.append(info);
		len += info.getBytes().length;
		return this;
	}

	public ServletResponse println(String info) {
		content.append(info).append(CRLF);
		len += info.getBytes().length + CRLF.length();
		return this;
	}

	/**
	 * @throws IOException
	 * @explain 将响应刷回浏览器
	 */
	public void flush(HttpStatus code) throws IOException {
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
			LogUtil.info("向浏览器返回数据成功");
		} finally {
			CloseUtil.close(out);
		}
	}

	public void addCookie(String key, String value) {
		cookies.put(key, value);
	}

	/**
	 * @explain 将map形式的cookie转化为字符串形式
	 */
	private void convertCookie() {
		Iterator<Entry<String, String>> it = cookies.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			cookieStr.append(key).append("=").append(value).append(";");
		}
	}

}
