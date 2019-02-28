package com.lanjian.utils;

import java.util.Collection;

import com.lanjian.constant.ContextConstant;

import eu.medsea.mimeutil.MimeUtil;

/**
 * @author lanjian
 * @date 2019年2月28日
 */
public class MimeTypeUtil {
	static {
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
	}

	public static String getTypes(String fileName) {
		if (fileName.endsWith(".html")) {
			return ContextConstant.DEFAULT_CONTENT_TYPE;
		}
		Collection<?> mimeTypes = MimeUtil.getMimeTypes(MimeTypeUtil.class.getResource(fileName));
		return mimeTypes.toArray()[0].toString();
	}

}
