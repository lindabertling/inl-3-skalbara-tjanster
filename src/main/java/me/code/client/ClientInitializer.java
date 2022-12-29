package me.code.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import me.code.Node;

public class ClientInitializer extends ChannelInitializer<SocketChannel>{

    private final Node node;
    private final Channel clientChannel;

    public ClientInitializer(Node node, Channel channel) {
        this.node = node;
        this.clientChannel = channel;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        var pipeline = socketChannel.pipeline();

        pipeline.addLast(new ClientHandler(node, clientChannel));
    }
}
