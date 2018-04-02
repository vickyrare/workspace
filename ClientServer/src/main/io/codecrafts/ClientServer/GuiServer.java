package io.codecrafts.ClientServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Vector;

/**
 * Created by waqqas on 4/1/2018.
 */
public class GuiServer {
    private JButton buttonStartServer;

    private JButton buttonStopServer;

    private JPanel panelServer;

    private JTextField textFieldPort;

    private JList listConnectedClients;

    ClientConnection clientConnection;

    private Thread serverThread;

    private ServerSocket serverSocket;

    private boolean serverRunning;

    public static DefaultListModel<Integer> connectedClients = new DefaultListModel<Integer>();

    public void addClient(int port) {
        if(!connectedClients.contains(port)) {
            System.out.println("Client connected on port " + port);
            connectedClients.addElement(port);
        }
    }

    public void removeClient(int port) {
        for(int i = 0; i < connectedClients.getSize(); i++) {
            if(port == connectedClients.get(i)) {
                connectedClients.remove(i);
            }
        }
    }

    public void removeAllClients() {
        if(connectedClients != null && !connectedClients.isEmpty()) {
            connectedClients.removeAllElements();
        }
    }

    public GuiServer() {
        serverRunning = false;
        textFieldPort.setText("6666");
        buttonStartServer.setEnabled(!serverRunning);
        buttonStopServer.setEnabled(serverRunning);

        listConnectedClients.setModel(connectedClients);

        buttonStartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!textFieldPort.getText().isEmpty() && !isServerRunning()) {
                    try {
                        start(Integer.parseInt(textFieldPort.getText()));
                        removeAllClients();
                        serverRunning = true;
                        buttonStopServer.setEnabled(serverRunning);
                        buttonStartServer.setEnabled(!serverRunning);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        buttonStopServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (isServerRunning()) {
                        stop();
                        removeAllClients();
                        serverRunning = false;
                        buttonStopServer.setEnabled(serverRunning);
                        buttonStartServer.setEnabled(!serverRunning);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void start(int port) throws IOException {
        clientConnection = new ClientConnection(this, serverSocket, port);
        serverThread = new Thread(clientConnection);
        serverThread.start();

        Thread guiThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(500);
                        //System.out.println("Thread running");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //guiThread.start();
    }

    public void stop() throws IOException {
        clientConnection.stopClientConnections();
        serverThread.interrupt();
    }

    public static void main(String []args) {
        JFrame jFrame= new JFrame("GUI Server");
        jFrame.setContentPane(new GuiServer().panelServer);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    private boolean isServerRunning() {
        return serverRunning;
    }

    private void setServerRunning(boolean serverRunning) {
        this.serverRunning = serverRunning;
    }
}
