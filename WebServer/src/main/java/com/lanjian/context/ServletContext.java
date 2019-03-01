package com.lanjian.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.lanjian.context.holder.ServletHolder;
import com.lanjian.servlet.Servlet;
import com.lanjian.utils.LogUtil;
import com.lanjian.utils.XMLUtil;

/**
 * @explain ServletContext是一个上下文对象，用于保存各种和servlet有关的配置信息
 * @author lanjian
 * @date 2019年3月1日
 */
@SuppressWarnings("unchecked")
public class ServletContext {
	// index --> IndexServlet 一对一
	private Map<String, ServletHolder> servlet;
	// /index --> index 多对一
	private Map<String, String> servletMapping;

	public ServletContext() {
		servlet = new HashMap<String, ServletHolder>();
		servletMapping = new HashMap<String, String>();
		// 用dom4j解析web.xml配置
		parseConfig();
	}

	/**
	 * @explain 用dom4j解析web.xml配置
	 */
	private void parseConfig() {
		try {
			LogUtil.info("正在解析web.xml配置文件......");
			Document document = XMLUtil.getDocument("web.xml");
			// 获取根节点
			Element root = document.getRootElement();
			// 获取根节点的所有名为servlet的子结点
			List<Element> servlets = root.elements("servlet");
			for (Element servletElement : servlets) {
				String servletName = servletElement.element("servlet-name").getTextTrim();
				String servletClass = servletElement.element("servlet-class").getTextTrim();
				this.servlet.put(servletName, new ServletHolder(servletClass));
			}
			List<Element> servletMapping = root.elements("servlet-mapping");
			for (Element mapping : servletMapping) {
				List<Element> urlPatterns = mapping.elements("url-pattern");
				String servletName = mapping.element("servlet-name").getTextTrim();
				for (Element urlPattern : urlPatterns) {
					String url = urlPattern.getTextTrim();
					this.servletMapping.put(url, servletName);
				}
			}
			LogUtil.info("web.xml配置解析成功");
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error("web.xml配置解析失败");
		}
	}

	/**
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @explain 根据不同的url，返回对应的servlet
	 */
	public Servlet getServlet(String url) {
		if (servletMapping.containsKey(url)) {
			// 根据url找到servletName
			String servletName = servletMapping.get(url);
			// 根据servletName找到对应servlet
			ServletHolder holder = servlet.get(servletName);
			if (holder.getServlet() == null) {
				try {
					Servlet servlet = (Servlet) Class.forName(holder.getServletClass()).newInstance();
					// 得到servlet之后设置进去，不用每次都反射得到
					holder.setServlet(servlet);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
					LogUtil.error("servlet反射实例化失败");
				}
			}
			return holder.getServlet();
		} else {
			LogUtil.info("找不到对应页面");
			return null;
		}
	}
}
