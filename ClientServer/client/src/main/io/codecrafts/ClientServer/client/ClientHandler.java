package io.codecrafts.ClientServer.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

/**
 * Handles a client-side channel.
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private ClientSocketListener socketListener;

    public ClientHandler(ClientSocketListener socketListener) {
        this.socketListener = socketListener;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        socketListener.onDataReceive(msg);
        System.err.println(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        socketListener.onDisconnect(inetSocketAddress.getPort());
        System.out.println("Client disconnected on port " + inetSocketAddress.getPort());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}