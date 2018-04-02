package io.codecrafts.ClientServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by waqqas on 4/1/2018.
 */
public class GuiClient {
    private JPanel panelServer;

    private JTextField textFieldPort;

    private JTextField textFieldMessage;

    private JButton buttonConnect;

    private JButton buttonDisconnect;

    private JButton buttonSendMessage;

    private Socket clientSocket;

    private PrintWriter out;

    private BufferedReader in;

    private boolean connected;


    public GuiClient() {
        connected = false;
        textFieldPort.setText("6666");
        buttonConnect.setEnabled(!connected);
        buttonDisconnect.setEnabled(connected);


        buttonConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!textFieldPort.getText().isEmpty() && !isConnected()) {
                    try {
                        startConnection("127.0.0.1", Integer.parseInt(textFieldPort.getText()));
                        connected = true;
                        buttonDisconnect.setEnabled(connected);
                        buttonConnect.setEnabled(!connected);
                        sendMessage("hello server");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        buttonDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (isConnected()) {
                        stopConnection();
                        connected = false;
                        buttonDisconnect.setEnabled(connected);
                        buttonConnect.setEnabled(!connected);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void startConnection(String ip, int port) throws IOException {
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
            this.textFieldMessage.setText(resp);
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

    public static void main(String []args) {
        JFrame jFrame= new JFrame("GUI Client");
        jFrame.setContentPane(new GuiClient().panelServer);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    private boolean isConnected() {
        return connected;
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
    }
}
