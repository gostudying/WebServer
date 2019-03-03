package com.lanjian.filter.impl;

import com.lanjian.filter.Filter;
import com.lanjian.filter.FilterChain;
import com.lanjian.request.ServletRequest;
import com.lanjian.response.ServletResponse;

public class MyFilter implements Filter {

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		System.out.println("你好");
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
