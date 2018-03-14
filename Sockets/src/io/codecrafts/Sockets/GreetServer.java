package io.codecrafts.Sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetServer {
	private ServerSocket serverSocket;

	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		while(true) {
			Socket clientSocket = serverSocket.accept();
			new Thread(new GreetThread(clientSocket)).start();
		}
	}

	public void stop() throws IOException {
		serverSocket.close();
	}
	public static void main(String[] args) throws IOException {
		GreetServer server = new GreetServer();
		server.start(6666);
	}
}