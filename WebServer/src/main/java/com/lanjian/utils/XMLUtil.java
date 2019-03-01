package com.lanjian.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * @author lanjian
 * @date 2019年2月28日
 */
public class XMLUtil {

	public static Document getDocument(String configName) {
		try {
			// 获取解析器
			SAXReader reader = new SAXReader();
			// 读取文件流
			return reader.read(XMLUtil.class.getClassLoader().getResourceAsStream(configName));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

}
