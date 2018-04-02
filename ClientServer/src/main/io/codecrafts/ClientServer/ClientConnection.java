package io.codecrafts.ClientServer;

import io.codecrafts.Sockets.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by waqqas on 4/1/2018.
 */
public class ClientConnection implements Runnable {
    private ServerSocket serverSocket;
    private int port;
    private GuiServer guiServer;

    public ClientConnection(GuiServer guiServer, ServerSocket serverSocket, int port) {
        this.guiServer = guiServer;
        this.serverSocket = serverSocket;
        this.port = port;
    }

    public void stopClientConnections() {
        if(serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            if (Thread.currentThread().isInterrupted()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Server is getting shutdown");
                break;
            }

            Socket clientSocket = null;
            try {
                System.out.println("Server is waiting for client connections");
                clientSocket = serverSocket.accept();
                guiServer.addClient(clientSocket.getPort());
                System.out.println("Client connection accepted");
            }
            catch (SocketException socketException) {
                System.out.println("Server is getting shutdown");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Thread t = new Thread(new ServerThread(clientSocket));
            t.start();
            try {
                t.join();
                t.sleep(1000);
                guiServer.removeClient(clientSocket.getPort());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
