package com.lanjian.response;

import static com.lanjian.constant.CharConstant.BLANK;
import static com.lanjian.constant.CharConstant.CRLF;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lanjian.constant.HttpStatus;
import com.lanjian.cookie.Cookie;
import com.lanjian.utils.CloseUtil;
import com.lanjian.utils.LogUtil;

/**
 * @explain 封装响应信息
 * @author lanjian
 * @date 2019年2月28日
 */
public class ServletResponse {
	// 输出流
	private BufferedWriter bufferWriter;
	// 正文
	private StringBuilder content;
	// 响应头
	private StringBuilder header;
	// 响应头信息
	private int status;
	private Date date;
	private String server;
	private String contentType;
	private String connection;
	private List<Cookie> cookies;
	// 正文字节数
	private int contentLength;
	private String characterEncoding;
	private String redirectUrl;

	public ServletResponse(Socket client) throws IOException {
		this.content = new StringBuilder();
		this.header = new StringBuilder();
		this.bufferWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		this.status = 200;
		this.server = "WebServer/1.0";
		this.connection = "close";
		this.date = new Date();
		this.cookies = new ArrayList<Cookie>();
		this.contentLength = 0;
		this.characterEncoding = "UTF-8";
		this.contentType = "text/html; charset=" + characterEncoding;
	}

	private void createStatusLine() {
		this.header.append("HTTP/1.1").append(BLANK).append(status).append(BLANK);
		switch (status) {
		case 200:
			header.append(HttpStatus.OK);
			break;
		case 302:
			header.append(HttpStatus.Redirect);
			break;
		case 400:
			header.append(HttpStatus.BadRequest);
			break;
		case 404:
			header.append(HttpStatus.NotFound);
			break;
		case 500:
			header.append(HttpStatus.ServerError);
			break;
		default:
			break;
		}
		header.append(CRLF);
	}

	private void createRedirectLine() {
		if (!StringUtils.isBlank(redirectUrl)) {
			this.header.append("Location:").append(redirectUrl).append(CRLF);
		}
	}

	private void createDateLine() {
		this.header.append("Date:").append(new Date()).append(CRLF);
	}

	private void createCookieLine() {
		if (!cookies.isEmpty()) {
			this.header.append("Set-Cookie:");
			for (Cookie cookie : cookies) {
				this.header.append(cookie.getKey()).append("=").append(cookie.getValue()).append(";");
			}
			this.header.append(CRLF);
		}
	}

	private void createServerLine() {
		this.header.append("Server:").append(server).append(CRLF);
	}

	private void createContentTypeLine() {
		this.header.append("Content-type:text/html;charset=").append(this.characterEncoding).append(CRLF);
	}

	public void createContentLengthLine() {
		this.header.append("Content-length:").append(contentLength).append(CRLF);
	}

	/**
	 * @explain 根据状态码构建响应头信息
	 */
	public void createHeader(int status) {
		setStatus(status);
		createStatusLine();
		createRedirectLine();
		createDateLine();
		createServerLine();
		createContentTypeLine();
		createContentLengthLine();
		createCookieLine();
		header.append(CRLF);
	}

	public void sendRedirect(String url) throws IOException {
		LogUtil.info("重定向至：" + url);
		setRedirectUrl(url);
		flush(HttpStatus.Redirect);
	}

	private void setRedirectUrl(String url) {
		this.redirectUrl = url;
	}

	public ServletResponse print(String info) {
		content.append(info);
		contentLength += info.getBytes().length;
		return this;
	}

	public ServletResponse println(String info) {
		content.append(info).append(CRLF);
		contentLength += info.getBytes().length + CRLF.length();
		return this;
	}

	/**
	 * @throws IOException
	 * @explain 将响应刷回浏览器
	 */
	public void flush(int status) throws IOException {
		try {
			// 创建头信息
			createHeader(status);
			// 添加响应头
			bufferWriter.append(header.toString());
			// 添加响应内容
			if (!StringUtils.isBlank(content)) {
				bufferWriter.append(content.toString());
			}
			bufferWriter.flush();
			LogUtil.info("向浏览器返回数据成功");
		} finally {
			CloseUtil.close(bufferWriter);
		}
	}

	public void addCookie(Cookie cookie) {
		cookies.add(cookie);
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getHeader() {
		return header.toString();
	}

	public String getContentType() {
		return contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	public int getStatus() {
		return status;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setDate(Date date) {
		this.date = date;

	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	public String getContent() {
		return content.toString();
	}

	public Date getDate() {
		return date;
	}

	public String getServer() {
		return server;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

}
