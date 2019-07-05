package muserver.utils;

import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

public class NettyUtils {
    private final static Logger logger = LogManager.getLogger(NettyUtils.class);

    public static void closeConnection(ChannelHandlerContext ctx) {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        if (remoteAddress != null) {
            logger.warn(String.format("Hacking attempt from: %s", remoteAddress.getAddress().getHostName()));
            ctx.close();
        }
    }
}
