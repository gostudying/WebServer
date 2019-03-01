package com.lanjian.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @explain 加载properties文件
 * @author lanjian
 * @date 2019年2月27日
 */
public class PropertiesUtil {

	private static Properties props;

	static {
		props = new Properties();
		loadProps();
	}

	private static void loadProps() {
		LogUtil.info("正在加载properties文件内容......");
		InputStream in = null;
		try {
			// resource目录下直接写文件名
			in = PropertiesUtil.class.getClassLoader().getResourceAsStream("server.properties");
			props.load(in);
		} catch (FileNotFoundException e) {
			LogUtil.error("server.properties文件未找到");
		} catch (IOException e) {
			LogUtil.error("server.properties文件读取失败");
		} finally {
			CloseUtil.close(in);
		}
		LogUtil.info("加载properties文件内容完成");
	}

	public static String getProperty(String key) {
		if (null == key) {
			loadProps();
		}
		return props.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		if (null == props) {
			loadProps();
		}
		return props.getProperty(key, defaultValue);
	}

}
