package io.codecrafts.ClientServer.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by waqqas on 4/1/2018.
 */
public class GuiClient extends JFrame implements ClientSocketListener {
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

    Bootstrap bootstrap;

    EventLoopGroup group;

    Channel channel;

    ChannelFuture lastWriteFuture;

    private boolean connected;

    private static DefaultListModel<String> receivedMessages = new DefaultListModel<String>();

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
                    btnDisconnect.setEnabled(connected);
                    btnConnect.setEnabled(!connected);
                    btnSendMessage.setEnabled(connected);
                    txtServerPort.setEnabled(false);
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
        group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitializer(this));

            // Start the connection attempt.
            channel = bootstrap.connect(ip, port).sync().channel();
            connected = true;
        } catch (Exception e) {
            stopConnection();
            System.out.println("Server is currently unavailable");
            JOptionPane.showMessageDialog(txtMessage.getRootPane(), "Server is currently unavailable.");
        }
    }

    public void stopConnection() {
        // Wait until all messages are flushed before closing the channel.
        if (lastWriteFuture != null) {
            try {
                lastWriteFuture.sync();
                lastWriteFuture = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(group != null) {
            group.shutdownGracefully();
            group = null;
        }
        connected = false;
        btnDisconnect.setEnabled(connected);
        btnConnect.setEnabled(!connected);
        btnSendMessage.setEnabled(connected);
        txtServerPort.setEnabled(true);
    }

    public void send(String msg) {
        // Send message to server
        lastWriteFuture = channel.writeAndFlush(msg);
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

    @Override
    public void onConnect(int port) {
    }

    @Override
    public void onDisconnect(int port) {
        System.out.println("Connection closed");
        JOptionPane.showMessageDialog(txtMessage.getRootPane(), "Connection closed.");
        stopConnection();
    }

    @Override
    public void onDataReceive(String msg) {
        receivedMessages.addElement(msg);
    }
}
