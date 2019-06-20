import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UdpConnectServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 private final static Logger logger = LogManager.getLogger(UdpConnectServerHandler.class);

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  ctx.flush();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
  logger.info("UDP CONNECT SERVER HANDLER");
  logger.info(packet.toString());
 }
}
