package wtf.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import wtf.util.Handler;

public class NetworkChannel {

    private ChannelHandlerContext ctx;

    private Handler<Void> closeHandler;
    private Handler<String> messageHandler;

    public NetworkChannel(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.ctx.pipeline().addLast(new WebSocketHandler());
    }

    public void write(String msg) {
        ctx.writeAndFlush(new TextWebSocketFrame(msg));
    }

    public void close() {
        ctx.writeAndFlush(new CloseWebSocketFrame()).addListener(f -> ctx.close());
    }

    public void onMessage(Handler<String> messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void onClose(Handler<Void> closeHandler) {
        this.closeHandler = closeHandler;
    }

    private final class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

        @Override
        public boolean acceptInboundMessage(Object msg) throws Exception {
            return msg instanceof TextWebSocketFrame;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            if (messageHandler != null) {
                messageHandler.handle(msg.text());
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (closeHandler != null) {
                closeHandler.handle(null);
            }
        }
    }
}
