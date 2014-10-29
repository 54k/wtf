package wtf.kernel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import wtf.util.Handler;

public class NetworkSessionImpl implements NetworkSession {

    private ChannelHandlerContext ctx;

    private Handler<Void> closeHandler;
    private Handler<String> messageHandler;
    private TaskManager taskManager;

    public NetworkSessionImpl(TaskManager taskManager, ChannelHandlerContext ctx) {
        this.taskManager = taskManager;
        this.ctx = ctx;
        this.ctx.pipeline().addLast(new WebSocketHandler());
    }

    @Override
    public void write(String msg) {
        ctx.writeAndFlush(new TextWebSocketFrame(msg));
    }

    @Override
    public void close() {
        ctx.writeAndFlush(new CloseWebSocketFrame()).addListener(f -> ctx.close());
    }

    @Override
    public void onMessage(Handler<String> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
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
            String text = msg.text();
            if (messageHandler != null) {
                taskManager.execute(() -> messageHandler.handle(text));
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if (closeHandler != null) {
                taskManager.execute(() -> closeHandler.handle(null));
            }
        }
    }
}
