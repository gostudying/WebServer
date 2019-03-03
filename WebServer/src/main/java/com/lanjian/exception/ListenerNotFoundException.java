package com.lanjian.exception;

import com.lanjian.constant.HttpStatus;
import com.lanjian.exception.base.ServletException;

/**
 * 
 * @explain 未找到对应的Listener（web.xml配置错误）
 * @author lanjian
 * @date 2019年3月3日
 */
public class ListenerNotFoundException extends ServletException {
	private static final int status = HttpStatus.NotFound;

	public ListenerNotFoundException() {
		super(status);
	}
}