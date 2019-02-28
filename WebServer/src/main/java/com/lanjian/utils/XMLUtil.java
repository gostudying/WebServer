package com.lanjian.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @author lanjian
 * @date 2019年2月28日
 */
public class XMLUtil {

	public static Document getDocument(InputStream in) {
		try {
			SAXReader reader = new SAXReader();
			return reader.read(in);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
}
