package com.lanjian.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.Element;

import com.lanjian.context.holder.FilterHolder;
import com.lanjian.context.holder.ServletHolder;
import com.lanjian.exception.ServletNotFoundException;
import com.lanjian.exception.base.ServletException;
import com.lanjian.filter.Filter;
import com.lanjian.response.ServletResponse;
import com.lanjian.servlet.http.HttpServlet;
import com.lanjian.session.HttpSession;
import com.lanjian.utils.LogUtil;
import com.lanjian.utils.UUIDUtil;
import com.lanjian.utils.XMLUtil;

/**
 * @explain 一个web项目，就存在一个ServletContext实例，每个Servlet读可以访问到它
 * @author lanjian
 * @date 2019年3月1日
 */
@SuppressWarnings("unchecked")
public class ServletContext {

	// index --> IndexServlet 一对一
	private Map<String, ServletHolder> servlets;
	// /index --> index 多对一
	private Map<String, String> servletMapping;

	// 过滤器
	private Map<String, FilterHolder> filters;
	// 一个url可以对应多个过滤器，但只能对应一个servlet
	private Map<String, List<String>> filterMapping;

	// web项目中所有的session
	private Map<String, HttpSession> sessions;

	// ServletContext域
	private Map<String, Object> attribute;

	public ServletContext() {
		servlets = new HashMap<String, ServletHolder>();
		servletMapping = new HashMap<String, String>();
		filters = new HashMap<String, FilterHolder>();
		filterMapping = new HashMap<String, List<String>>();
		sessions = new HashMap<String, HttpSession>();
		attribute = new ConcurrentHashMap<String, Object>();
		// 用dom4j解析web.xml配置
		parseConfig();
	}

	public Object getAttribute(String name) {
		return attribute.get(name);
	}

	public void setAttribute(String name, Object obj) {
		attribute.put(name, obj);
	}

	public void removeAttribute(String name) {
		if (attribute.containsKey(name)) {
			attribute.remove(name);
		}
	}

	/**
	 * @explain 用dom4j解析web.xml配置文件
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
				this.servlets.put(servletName, new ServletHolder(servletClass));
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
			// 解析 filter
			List<Element> filters = root.elements("filter");
			for (Element filterEle : filters) {
				String filterName = filterEle.element("filter-name").getTextTrim();
				String filterClass = filterEle.element("filter-class").getTextTrim();
				this.filters.put(filterName, new FilterHolder(filterClass));
			}

			List<Element> filterMapping = root.elements("filter-mapping");
			for (Element mapping : filterMapping) {
				List<Element> urlPatterns = mapping.elements("url-pattern");
				String value = mapping.element("filter-name").getTextTrim();
				for (Element urlPattern : urlPatterns) {
					List<String> values = this.filterMapping.get(urlPattern.getTextTrim());
					if (values == null) {
						values = new ArrayList<>();
						this.filterMapping.put(urlPattern.getText(), values);
					}
					values.add(value);
				}
			}
			LogUtil.info("web.xml配置解析成功");
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error("web.xml配置解析失败");
		}
	}

	/**
	 * @throws ServletException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @explain 根据不同的url，返回对应的servlet
	 */
	public HttpServlet getServlet(String url) throws ServletNotFoundException {
		if (servletMapping.containsKey(url)) {
			// 根据url找到servletName
			String servletName = servletMapping.get(url);
			// 根据servletName找到对应servlet
			ServletHolder holder = servlets.get(servletName);
			if (holder.getServlet() == null) {
				try {
					HttpServlet servlet = (HttpServlet) Class.forName(holder.getServletClass()).newInstance();
					// 得到servlet之后设置进去，不用每次都反射得到
					holder.setServlet(servlet);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
					LogUtil.error("servlet反射实例化失败");
					throw new ServletNotFoundException();
				}
			}
			if (holder.getServlet() == null) {
				throw new ServletNotFoundException();
			}
			return holder.getServlet();
		} else {
			LogUtil.info("该路径没有对应的Servlet");
			throw new ServletNotFoundException();
		}
	}

	/**
	 * @explain 根据url找到对应的filters
	 */
	public List<Filter> getFilters(String url) {
		List<Filter> filterlist = new ArrayList<>();
		if (filterMapping.containsKey(url)) {
			List<String> filterNames = filterMapping.get(url);
			for (String filterName : filterNames) {
				FilterHolder holder = filters.get(filterName);
				Filter filter = holder.getFilter();
				if (filter == null) {
					try {
						filter = (Filter) Class.forName(holder.getFilterClass()).newInstance();
						// 得到filter之后设置进去，不用每次都反射得到
						holder.setFilter(filter);
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						e.printStackTrace();
						LogUtil.error("filter反射实例化失败");
					}
				}
				filterlist.add(filter);
			}
		} else {
			LogUtil.info("该路径没有对应的过滤器");
		}
		return filterlist;
	}

	/**
	 * @explain 创建session
	 */
	public HttpSession createSession(ServletResponse response) {
		HttpSession session = new HttpSession(UUIDUtil.uuid());
		sessions.put(session.getId(), session);
		response.addCookie("JSESSIONID", session.getId());
		return session;
	}

	/**
	 * @explain 获取session
	 */
	public HttpSession getSession(String JSESSIONID) {
		return sessions.get(JSESSIONID);
	}

	/**
	 * @explain 使session无效
	 */
	public void invalidateSession(HttpSession session) {
		sessions.remove(session.getId());
	}

	/**
	 * @explain web关闭前被调用
	 */
	public void destroy() {
		servlets.values().forEach(servletHolder -> {
			if (servletHolder.getServlet() != null) {
				servletHolder.getServlet().destroy();
			}
		});
		filters.values().forEach(filterHolder -> {
			if (filterHolder.getFilter() != null) {
				filterHolder.getFilter().destroy();
			}
		});
	}
}
