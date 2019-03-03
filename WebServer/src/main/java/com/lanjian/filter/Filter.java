package com.lanjian.filter;

import com.lanjian.request.ServletRequest;
import com.lanjian.response.ServletResponse;

/**
 * @explain 过滤器
 * @author lanjian
 * @date 2019年3月3日
 */
public interface Filter {
	/**
	 * 过滤器初始化
	 */
	void init();

	void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain);

	/**
	 * 过滤器销毁
	 */
	void destroy();
}
