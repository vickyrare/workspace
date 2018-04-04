package io.codecrafts.ClientServer.server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by waqqas on 4/1/2018.
 */
public class GuiServer extends JFrame{
    private JPanel contentPane;

    private JTextField txtServerPort;

    private JScrollPane scrollPaneClientConnections;

    private JList listClientConnections;

    private JLabel lblClientConnections;

    private JPanel panelStartStopServer;

    private JButton btnStartServer;

    private JButton btnStopServer;

    ClientConnection clientConnection;

    private Thread serverThread;

    private ServerSocket serverSocket;

    private boolean serverRunning;

    private Map<Integer, Socket> clientConnections = new HashMap<Integer, Socket>();

    public static DefaultListModel<Integer> connectedClients = new DefaultListModel<Integer>();

    public void addClient(int port, Socket clientSocket) {
        if(!connectedClients.contains(port)) {
            System.out.println("Client connected on port " + port);
            connectedClients.addElement(port);
        }

        if(!clientConnections.containsKey(port)) {
            clientConnections.put(port, clientSocket);
        }
    }

    public void removeClient(int port) {
        for(int i = 0; i < connectedClients.getSize(); i++) {
            if(port == connectedClients.get(i)) {
                connectedClients.remove(i);
                break;
            }
        }
        if (clientConnections.containsKey(port)) {
            Socket clientSocket = clientConnections.remove(port);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeAllClients() {
        if(connectedClients != null && !connectedClients.isEmpty()) {
            connectedClients.removeAllElements();
        }

        for(Socket clientSocket: clientConnections.values()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(clientConnections != null) {
            clientConnections.clear();
        }
    }

    public GuiServer() {
        setTitle("GUI Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 732, 250);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JLabel lblServerPort = new JLabel("Server Port");
        GridBagConstraints gbc_lblServerPort = new GridBagConstraints();
        gbc_lblServerPort.insets = new Insets(0, 0, 5, 5);
        gbc_lblServerPort.anchor = GridBagConstraints.WEST;
        gbc_lblServerPort.gridx = 1;
        gbc_lblServerPort.gridy = 0;
        contentPane.add(lblServerPort, gbc_lblServerPort);

        txtServerPort = new JTextField();
        GridBagConstraints gbc_txtServerPort = new GridBagConstraints();
        gbc_txtServerPort.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtServerPort.insets = new Insets(0, 0, 5, 0);
        gbc_txtServerPort.gridx = 2;
        gbc_txtServerPort.gridy = 0;
        contentPane.add(txtServerPort, gbc_txtServerPort);
        txtServerPort.setColumns(10);

        lblClientConnections = new JLabel("Client Connections");
        GridBagConstraints gbc_lblClientConnections = new GridBagConstraints();
        gbc_lblClientConnections.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblClientConnections.insets = new Insets(0, 0, 5, 5);
        gbc_lblClientConnections.gridx = 1;
        gbc_lblClientConnections.gridy = 1;
        contentPane.add(lblClientConnections, gbc_lblClientConnections);

        scrollPaneClientConnections = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollPane.anchor = GridBagConstraints.NORTH;
        gbc_scrollPane.gridx = 2;
        gbc_scrollPane.gridy = 1;
        contentPane.add(scrollPaneClientConnections, gbc_scrollPane);

        listClientConnections = new JList();
        scrollPaneClientConnections.setViewportView(listClientConnections);

        panelStartStopServer = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.anchor = GridBagConstraints.NORTH;
        gbc_panel.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel.gridx = 2;
        gbc_panel.gridy = 2;
        contentPane.add(panelStartStopServer, gbc_panel);

        btnStartServer = new JButton("Start Server");
        panelStartStopServer.add(btnStartServer);

        btnStopServer = new JButton("Stop Server");
        panelStartStopServer.add(btnStopServer);

        serverRunning = false;
        txtServerPort.setText("6666");
        btnStartServer.setEnabled(!serverRunning);
        btnStopServer.setEnabled(serverRunning);

        listClientConnections.setModel(connectedClients);

        btnStartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txtServerPort.getText().isEmpty() && !isServerRunning()) {
                    try {
                        start(Integer.parseInt(txtServerPort.getText()));
                        removeAllClients();
                        serverRunning = true;
                        btnStopServer.setEnabled(serverRunning);
                        btnStartServer.setEnabled(!serverRunning);
                        txtServerPort.setEnabled(false);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        btnStopServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (isServerRunning()) {
                        stop();
                        removeAllClients();
                        serverRunning = false;
                        btnStopServer.setEnabled(serverRunning);
                        btnStartServer.setEnabled(!serverRunning);
                        txtServerPort.setEnabled(true);
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

        Thread connectionWatcherThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(500);
                        for(Socket clientSocket: clientConnections.values()) {
                            if(clientSocket.isClosed()) {
                                removeClient(clientSocket.getPort());
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        connectionWatcherThread.start();
    }

    public void stop() throws IOException {
        clientConnection.stopClientConnections();
        serverThread.interrupt();
    }

    public static void main(String []args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GuiServer frame = new GuiServer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isServerRunning() {
        return serverRunning;
    }

    private void setServerRunning(boolean serverRunning) {
        this.serverRunning = serverRunning;
    }
}
