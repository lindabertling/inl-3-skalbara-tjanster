package me.code.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import me.code.Node;
import me.code.NodeHandler;

public class NodeStartupInitializer extends ChannelInitializer<SocketChannel> {

    private final Node node;
    private final NodeHandler nodeHandler;

    public NodeStartupInitializer(Node node, NodeHandler nodeHandler) {
        this.node = node;
        this.nodeHandler = nodeHandler;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel
                .pipeline()
                .addLast(new SimpleChannelInboundHandler<ByteBuf>() {

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        nodeHandler.addStartedNode(node);
                        ctx.close();
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

                    }
                });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
