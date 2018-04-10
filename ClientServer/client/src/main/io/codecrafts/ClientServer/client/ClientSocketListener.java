package io.codecrafts.ClientServer.client;

public interface ClientSocketListener {
    public void onConnect(int port);
    public void onDisconnect(int port);
    public void onDataReceive(String msg);
}
