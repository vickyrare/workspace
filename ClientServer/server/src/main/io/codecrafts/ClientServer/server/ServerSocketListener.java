package io.codecrafts.ClientServer.server;

public interface ServerSocketListener {
    public void onConnect(String clientAddress);
    public void onDisconnect(String clientAddress);
}
