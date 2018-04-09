package io.codecrafts.ClientServer.server;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by waqqas on 4/1/2018.
 */
public class ClientConnection implements Runnable {
    private Selector selector;
    private int port;
    private GuiServer guiServer;

    public ClientConnection(GuiServer guiServer, Selector selector) {
        this.guiServer = guiServer;
        this.selector = selector;
    }

    @Override
    public void run() {
        while(true && selector.isOpen()) {
            // wait for events
            int readyCount = 0;
            try {
                readyCount = selector.select();
            } catch (IOException e) {
                System.out.println("Server is shutting down");
            }
            if (readyCount == 0) {
                continue;
            }

            // process selected keys...
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                // Remove key from set so we don't process it twice
                iterator.remove();

                if (!key.isValid()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    guiServer.removeClient(channel.socket().getPort());
                    continue;
                }

                if (key.isAcceptable()) { // Accept client connections
                    try {
                        accept(key);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (key.isReadable()) { // Read from client
                    try {
                        read(key);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (key.isWritable()) {
                    // write data to client...
                }
            }
        }
    }

    // accept client connection
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket clientSocket = channel.socket();
        SocketAddress remoteAddr = clientSocket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);
        guiServer.addClient(channel.socket().getPort(), clientSocket);

		/*
         * Register channel with selector for further IO (record it for read/write
		 * operations, here we have used read operation)
		 */
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    // read from the socket channel
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            guiServer.removeClient(socket.getPort());
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);
        System.out.println("Got from client: " + new String(data));

        if ("hello server".equals(new String(data))) {
            write(key, "hello client");
        } else {
            write(key, "unrecognised greeting");
        }
    }

    // write to the socket channel
    private void write(SelectionKey key, String msg) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(74);
        buffer.put(msg.getBytes());
        buffer.flip();
        channel.write(buffer);
        System.out.println(msg);
        buffer.clear();
    }
}
