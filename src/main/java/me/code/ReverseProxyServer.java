package me.code;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class ReverseProxyServer {

    private final int port;
    private final EventLoopGroup bossGroup, workerGroup;

    public ReverseProxyServer(int port) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
    }

    public void start() {
        var bootstrap = new ServerBootstrap();

        try {
            bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            var pipeline = socketChannel.pipeline();

                            pipeline.addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(1024))
                                    .addLast(new ProxyHandler(ReverseProxyServer.this));
                        }
                    })
                    .bind(port).sync().channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }
}
