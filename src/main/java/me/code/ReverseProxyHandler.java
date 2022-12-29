package me.code;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ReverseProxyHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final ReverseProxyServer server;

//    public static List<Integer> PORTS = List.of(8080, 8081);

    public ReverseProxyHandler(ReverseProxyServer server) {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {

        var original = ctx.channel();

        var bootstrap = new Bootstrap();

        var node = NodeHandler.getInstance().next();

//        var random = ThreadLocalRandom.current().nextInt(0, PORTS.size());
//        var port = PORTS.get(random);

        try {
            var channel  = bootstrap
                    .group(server.getWorkerGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            var pipeline = socketChannel.pipeline();

                            pipeline.addLast(new ClientHandler(original));

                        }
                    })
                    .connect("localhost", node.getPort()).sync().channel();

            channel.writeAndFlush(buf.copy());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        var host = request.headers().get("destination").split(":");
        var address = host[0];
        var port = Integer.parseInt(host[1]);

        request.headers().remove("destination");
        var copy = request.copy(); // request som ska skickas vidare till tjänsten
        var original = ctx.channel(); // klienten från början (t.ex postman), behövs för att skicka tillbaka responsen

        var bootstrap = new Bootstrap();

        try {
            bootstrap
                    .group(server.getWorkerGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            var pipeline = socketChannel.pipeline();

                            pipeline.addLast(new HttpRequestEncoder())
                                    .addLast(new HttpResponseDecoder())
                                    .addLast(new HttpObjectAggregator(1024))
                                    .addLast(new ClientHandler(original, copy));

                        }
                    })
                    .connect(address, port);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/
}
