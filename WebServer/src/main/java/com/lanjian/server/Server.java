package com.lanjian.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.lanjian.request.Request;
import com.lanjian.response.Response;

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
			Request request = new Request(client.getInputStream());

			// 构造响应
			Response response = new Response(client.getOutputStream());
			response.println("<h1>hello webserver</h1>");
			response.println("<h2>欢迎：</h2>").println(request.getParameter("name"));
			response.pushToClient("200");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}
