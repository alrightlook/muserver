import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UdpConnectServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 private final static Logger logger = LogManager.getLogger(UdpConnectServerHandler.class);

 @Override
 protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
  logger.info(datagramPacket.toString());
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  super.channelActive(ctx);
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  super.channelInactive(ctx);
 }
}
