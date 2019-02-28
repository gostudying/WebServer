package com.lanjian.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lanjian
 * @date 2019年2月28日
 */
public class IOUtil {

	public static byte[] getBytesFromFile(String fileName) throws IOException {
		InputStream in = IOUtil.class.getResourceAsStream(fileName);
		if (in == null) {
			LogUtil.info("Not Found File:" + fileName);
			throw new FileNotFoundException();
		}
		LogUtil.info("正在读取文件:" + fileName);
		return getBytesFromStream(in);
	}

	public static byte[] getBytesFromStream(InputStream in) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		CloseUtil.close(outStream, in);
		return outStream.toByteArray();
	}

}
