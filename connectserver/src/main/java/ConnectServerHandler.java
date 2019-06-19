import enums.Type;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import messages.ByteHeader;
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
        byte[] buffer = (byte[]) msg;

        switch (buffer[0]) {

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clients.put(ctx.channel().id(), Client.create(clients.size()));
        byte[] bytes = ConnectionResponse.create(ByteHeader.create(Type.C1, (byte) 0x4, (byte) 0x0), (byte) 0x1).serialize(new ByteArrayOutputStream());
        ctx.writeAndFlush(bytes);
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
