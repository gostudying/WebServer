package com.lanjian.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	ServerSocket serverSocket;

	public void start() {
		try {
			serverSocket = new ServerSocket(8888);
			receive();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receive() {
		try {
			Socket client = serverSocket.accept();
			String msg = null;// 接收客户端请求消息
			StringBuilder sb = new StringBuilder();
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

			while ((msg = bufferReader.readLine()).length() > 0) {
				sb.append(msg);
				sb.append("\r\n");
			}
			
			String requestInfo = sb.toString().trim();
			System.out.println(requestInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}
