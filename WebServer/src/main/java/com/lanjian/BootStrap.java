package com.lanjian;

import java.util.Scanner;

import com.lanjian.server.Server;
import com.lanjian.utils.PropertiesUtil;

/**
 * @explain 服务器入口
 * @author lanjian
 * @date 2019年2月27日
 */
public class BootStrap {

	public static void main(String[] args) {
		// 获取端口号
		String port = PropertiesUtil.getProperty("server.port");
		if (port == null) {
			throw new IllegalArgumentException("server.port 不存在");
		}
		Server server = new Server();
		// 服务器启动
		server.start(Integer.parseInt(port));
		// 用户输入EXIT，则关闭服务器
		Scanner in = new Scanner(System.in);
		String order;
		while (in.hasNext()) {
			order = in.next();
			if (order.equalsIgnoreCase("EXIT")) {
				server.close();
				in.close();
				System.exit(0);
			}
		}
	}
}
