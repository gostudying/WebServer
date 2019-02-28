package com.lanjian.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
	private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

	public static void info(String msg) {
		logger.info(msg);
	}

	public static void error(String msg) {
		logger.error(msg);
	}
}
