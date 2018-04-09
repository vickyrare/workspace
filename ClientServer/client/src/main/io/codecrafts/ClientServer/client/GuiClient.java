package io.codecrafts.ClientServer.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by waqqas on 4/1/2018.
 */
public class GuiClient extends JFrame {
    private JPanel contentPane;

    private JTextField txtServerPort;

    private JScrollPane scrollPaneReceivedMessages;

    private JList listReceivedMessages;

    private JLabel lblReceivedMessages;

    private JPanel panelConnectDisconnect;

    private JButton btnConnect;

    private JButton btnDisconnect;

    private JPanel panelSendMessage;

    private JTextField txtMessage;

    private JButton btnSendMessage;

    SocketChannel socketChannel;

    private BufferedInputStream in;

    private BufferedOutputStream out;

    private static final int BUFFER_SIZE = 1024;

    private boolean connected;

    public static DefaultListModel<String> receivedMessages = new DefaultListModel<String>();

    public GuiClient() {
        setTitle("GUI Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 732, 293);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
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
        gbc_txtServerPort.anchor = GridBagConstraints.WEST;
        gbc_txtServerPort.insets = new Insets(0, 0, 5, 0);
        gbc_txtServerPort.gridx = 2;
        gbc_txtServerPort.gridy = 0;
        contentPane.add(txtServerPort, gbc_txtServerPort);
        txtServerPort.setColumns(10);

        lblReceivedMessages = new JLabel("Received Messages");
        GridBagConstraints gbc_lblReceivedMessages = new GridBagConstraints();
        gbc_lblReceivedMessages.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblReceivedMessages.insets = new Insets(0, 0, 5, 5);
        gbc_lblReceivedMessages.gridx = 1;
        gbc_lblReceivedMessages.gridy = 1;
        contentPane.add(lblReceivedMessages, gbc_lblReceivedMessages);

        scrollPaneReceivedMessages = new JScrollPane();
        GridBagConstraints gbc_scrollPaneReceivedMessages = new GridBagConstraints();
        gbc_scrollPaneReceivedMessages.anchor = GridBagConstraints.NORTH;
        gbc_scrollPaneReceivedMessages.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPaneReceivedMessages.fill = GridBagConstraints.HORIZONTAL;
        gbc_scrollPaneReceivedMessages.gridx = 2;
        gbc_scrollPaneReceivedMessages.gridy = 1;
        contentPane.add(scrollPaneReceivedMessages, gbc_scrollPaneReceivedMessages);

        listReceivedMessages = new JList();
        scrollPaneReceivedMessages.setViewportView(listReceivedMessages);

        panelSendMessage = new JPanel();
        GridBagConstraints gbc_panelSendMessage = new GridBagConstraints();
        gbc_panelSendMessage.anchor = GridBagConstraints.WEST;
        gbc_panelSendMessage.insets = new Insets(0, 0, 5, 0);
        gbc_panelSendMessage.gridx = 2;
        gbc_panelSendMessage.gridy = 2;
        contentPane.add(panelSendMessage, gbc_panelSendMessage);

        txtMessage = new JTextField();
        panelSendMessage.add(txtMessage);
        txtMessage.setColumns(40);

        btnSendMessage = new JButton("Send Message");
        panelSendMessage.add(btnSendMessage);

        panelConnectDisconnect = new JPanel();
        GridBagConstraints gbc_panelConnectDisconnect = new GridBagConstraints();
        gbc_panelConnectDisconnect.anchor = GridBagConstraints.NORTH;
        gbc_panelConnectDisconnect.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelConnectDisconnect.gridx = 2;
        gbc_panelConnectDisconnect.gridy = 3;
        contentPane.add(panelConnectDisconnect, gbc_panelConnectDisconnect);

        btnConnect = new JButton("Connect");
        panelConnectDisconnect.add(btnConnect);

        btnDisconnect = new JButton("Disconnect");
        panelConnectDisconnect.add(btnDisconnect);

        connected = false;
        txtServerPort.setText("6666");
        btnConnect.setEnabled(!connected);
        btnDisconnect.setEnabled(connected);
        btnSendMessage.setEnabled(connected);

        listReceivedMessages.setModel(receivedMessages);

        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txtServerPort.getText().isEmpty() && !isConnected()) {
                    startConnection("127.0.0.1", Integer.parseInt(txtServerPort.getText()));
                    if(socketChannel != null && socketChannel.isConnected()) {
                        connected = true;
                        btnDisconnect.setEnabled(connected);
                        btnConnect.setEnabled(!connected);
                        btnSendMessage.setEnabled(connected);
                        txtServerPort.setEnabled(false);
                    }
                }
            }
        });

        btnDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if (isConnected()) {
                stopConnection();
            }
                   }
        });

        btnSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if (connected) {
                send(txtMessage.getText());
                txtMessage.setText("");
            }
            }
        });
    }

    public void startConnection(String ip, int port) {
        InetSocketAddress hostAddress = new InetSocketAddress(ip, port);
        try {
            socketChannel = SocketChannel.open(hostAddress);

            if (!socketChannel.isConnected()) {
                System.out.println("Server is currently unavailable");
                JOptionPane.showMessageDialog(txtMessage.getRootPane(), "Server is currently unavailable.");
                return;
            }

            setConnected(true);
            Thread clientThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(socketChannel.isConnected()) {
                        while(true) {
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int numRead = -1;
                            try {
                                System.out.println("Waiting for data from server.");
                                numRead = socketChannel.read(buffer);
                            } catch (IOException e) {
                                stopConnection();
                                return;
                            }

                            if (numRead == -1) {
                                stopConnection();
                                System.out.println("Server is currently unavailable");
                                JOptionPane.showMessageDialog(txtMessage.getRootPane(), "Server is currently unavailable.");
                                return;
                            }

                            byte[] data = new byte[numRead];
                            System.arraycopy(buffer.array(), 0, data, 0, numRead);
                            String dataStr = new String(data);
                            System.out.println("Got from server: " + dataStr);
                            receivedMessages.addElement(dataStr);
                        }
                    }
                }
            });
            clientThread.start();
        } catch (IOException e) {
            stopConnection();
            System.out.println("Server is currently unavailable");
            JOptionPane.showMessageDialog(txtMessage.getRootPane(), "Server is currently unavailable.");;
        }
    }

    public void stopConnection() {
        if(socketChannel != null) {
            try {
                socketChannel.close();
                setConnected(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            connected = false;
            btnDisconnect.setEnabled(connected);
            btnConnect.setEnabled(!connected);
            btnSendMessage.setEnabled(connected);
            txtServerPort.setEnabled(true);
        }
    }

    public void send(String msg) {
        // Send messages to server
        ByteBuffer buffer = ByteBuffer.allocate(74);
        buffer.put(msg.getBytes());
        buffer.flip();
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(msg);
        buffer.clear();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final GuiClient clientFrame = new GuiClient();
                    clientFrame.setVisible(true);
                    clientFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                            if(clientFrame.isConnected()) {
                                clientFrame.stopConnection();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isConnected() {
        return connected;
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
    }
}
