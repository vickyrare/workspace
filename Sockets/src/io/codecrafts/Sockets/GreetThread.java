package io.codecrafts.Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GreetThread implements Runnable {
	
	private PrintWriter out;
	private BufferedReader in;
	private Socket clientSocket;
	
	public GreetThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String greeting = null;
		try {
			greeting = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ("hello server".equals(greeting)) {
			out.println("hello client");
		}
		else {
			out.println("unrecognised greeting");
		}
		
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.close();
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
