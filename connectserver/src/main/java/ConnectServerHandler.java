import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectServerHandler extends ChannelInboundHandlerAdapter {
    private final ConcurrentHashMap<ChannelId, ConnectClient> clients = new ConcurrentHashMap<>();

    private final static Logger logger = LogManager.getLogger(ConnectServerHandler.class);

    public ConnectServerHandler() {
        super();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.put(ctx.channel().id(), ConnectClient.create(ctx, 0));


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clients.remove(ctx.channel().id());


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
