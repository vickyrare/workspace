package io.codecrafts.Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	public void startConnection(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		clientSocket.setSoTimeout(1000);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	public String sendMessage(String msg) {
		out.println(msg);
		String resp = null;
		try {
			resp = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	public void stopConnection() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
	}

	public static void main(String[] args) throws IOException {
		Client client = new Client();
		client.startConnection("127.0.0.1", 6666);
		String response = client.sendMessage("hello server");
		if(response  == null) {
			client.stopConnection();
			System.out.println("Server not available");
		} else {
			System.out.println(response);
		}
	}
}