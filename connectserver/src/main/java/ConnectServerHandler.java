import enums.PacketType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import messages.C1C3Header;
import messages.ConnectionResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectServerHandler extends ChannelInboundHandlerAdapter {
    private final ConcurrentHashMap<ChannelId, Client> clients = new ConcurrentHashMap<>();

    private final static Logger logger = LogManager.getLogger(ConnectServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.put(ctx.channel().id(), Client.create(clients.size()));
        ctx.writeAndFlush(ConnectionResponse.create(C1C3Header.create(PacketType.C1, (byte) 4, (byte) 0), (byte) 1).serialize(new ByteArrayOutputStream()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clients.remove(ctx.channel().id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
