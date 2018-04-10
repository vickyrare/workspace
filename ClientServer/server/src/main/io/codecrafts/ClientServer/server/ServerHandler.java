package io.codecrafts.ClientServer.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;

/**
 * Handles a server-side channel.
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private ServerSocketListener socketListener;

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public ServerHandler(ServerSocketListener socketListener) {
        this.socketListener = socketListener;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientAddress = inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort();
        socketListener.onConnect(clientAddress);
        System.out.println("Client connected on " + clientAddress);
        //socketListener.getConnectedClients().addElement(inetSocketAddress.getPort());
//        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
//                new GenericFutureListener<Future<Channel>>() {
//                    @Override
//                    public void operationComplete(Future<Channel> future) throws Exception {
//                        ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat service!\n");
//                        channels.add(ctx.channel());
//                    }
//                });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientAddress = inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort();
        socketListener.onDisconnect(clientAddress);
        System.out.println("Client disconnected on " + inetSocketAddress);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // Close the connection if the client has sent 'bye'.
        if ("bye".equals(msg.toLowerCase())) {
            ctx.close();
        }
        if("hello server".equals(msg)) {
            ctx.channel().writeAndFlush("hello client");
        } else {
            ctx.channel().writeAndFlush("unrecognised greeting");
        }
        // Send the received message to all channels but the current one.
        /*for (Channel c: channels) {
            if (c != ctx.channel()) {
                c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
            } else {
                c.writeAndFlush("[you] " + msg + '\n');
            }
        }*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
