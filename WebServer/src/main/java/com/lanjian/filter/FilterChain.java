package com.lanjian.filter;

import com.lanjian.request.ServletRequest;
import com.lanjian.response.ServletResponse;

/**
 * @author lanjian
 * @date 2019年3月3日
 */
public interface FilterChain {
	/**
	 * @explain 当前filter放行，由后续的filter继续进行过滤
	 */
	void doFilter(ServletRequest request, ServletResponse response);
}
