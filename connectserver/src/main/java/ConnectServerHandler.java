import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.ObjectName;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectServerHandler extends ChannelInboundHandlerAdapter {
    private final ConcurrentHashMap<ChannelId, ConnectClient> clients = new ConcurrentHashMap<>();

    private final static Logger logger = LogManager.getLogger(ConnectServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String logString = "OK, I got your client message";
        logger.info(logString);
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.put(ctx.channel().id(), ConnectClient.create(ctx, 0));
        String logString = "Hello, from server";
        ctx.writeAndFlush(logString);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
