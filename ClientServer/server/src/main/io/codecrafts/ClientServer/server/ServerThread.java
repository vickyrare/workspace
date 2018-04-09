package io.codecrafts.ClientServer.server;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {
	
	private BufferedInputStream in;
	private BufferedOutputStream out;
	private static final int BUFFER_SIZE = 1024;
	private Socket clientSocket;
	
	public ServerThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			out = new BufferedOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in = new BufferedInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String greeting = null;
		while(clientSocket.isConnected() && !clientSocket.isClosed()) {
			int nofBytesRead;
			byte[] buffer = new byte[BUFFER_SIZE];

			try {
				nofBytesRead = in.read(buffer, 0, BUFFER_SIZE);
				if(nofBytesRead == -1) {
					clientSocket.close();
					return;
				}

				greeting = new String(buffer, 0, nofBytesRead);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if ("hello server".equals(greeting)) {
				try {
					out.write("hello client".getBytes());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					out.write("unrecognised greeting".getBytes());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
