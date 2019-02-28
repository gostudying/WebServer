package com.lanjian.utils;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
	public static void close(Closeable... closeables) {
		if (closeables == null) {
			return;
		}
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (IOException e) {
					LogUtil.error("资源关闭失败");
				}
			}
		}
	}

}
