package com.lanjian.filter.impl;

import com.lanjian.filter.Filter;
import com.lanjian.filter.FilterChain;
import com.lanjian.request.ServletRequest;
import com.lanjian.response.ServletResponse;

public class MyFilter2 implements Filter {

	@Override
	public void init() {
		System.out.println("过滤器2初始化");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		System.out.println("你好2");
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
