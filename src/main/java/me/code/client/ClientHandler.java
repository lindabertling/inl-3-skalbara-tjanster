package me.code.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.code.Node;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final Channel channel;
    private final Node node;

    public ClientHandler(Node node, Channel channel) {
        this.channel = channel;
        this.node = node;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        channel.writeAndFlush(buf.copy());
        node.addRequest();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        node.addConnection(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        node.removeConnection(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        channel.close();
    }
}
