import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectServerHandler extends ChannelInboundHandlerAdapter {
    private final static Logger logger = LogManager.getLogger(ConnectServerHandler.class);

    public ConnectServerHandler() {
        super();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("public void channelRegistered(ChannelHandlerContext ctx) throws Exception");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("public void channelUnregistered(ChannelHandlerContext ctx) throws Exception");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("public void channelActive(ChannelHandlerContext ctx) throws Exception");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("public void channelInactive(ChannelHandlerContext ctx) throws Exception");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("public void channelReadComplete(ChannelHandlerContext ctx) throws Exception");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception");
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void ensureNotSharable() {
        logger.info("protected void ensureNotSharable()");
        super.ensureNotSharable();
    }

    @Override
    public boolean isSharable() {
        logger.info("public boolean isSharable()");
        return super.isSharable();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info(" public void handlerAdded(ChannelHandlerContext ctx) throws Exception");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("public void handlerRemoved(ChannelHandlerContext ctx) throws Exception");
        super.handlerRemoved(ctx);
    }
}
