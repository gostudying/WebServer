package com.lanjian.utils;

import java.util.UUID;

/**
 * @author lanjian
 * @date 2019年2月28日
 */
public class UUIDUtil {
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
}
