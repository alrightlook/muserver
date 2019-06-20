import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import messages.AbstractPacket;
import messages.PMSG_SERVERINFO;
import messages.SDHP_GAMESERVERINFO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

public class UdpConnectServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
 private final static byte C1 = (byte) 0xC1;
 private final static byte C2 = (byte) 0xC2;
 private final static byte C3 = (byte) 0xC3;
 private final static byte C4 = (byte) 0xC4;
 private final static Logger logger = LogManager.getLogger(UdpConnectServerHandler.class);

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  ctx.flush();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
  logger.debug(packet.toString());

  ByteBuf content = packet.content();

  if (content.readableBytes() == 0) {
   InetSocketAddress socketAddr = (InetSocketAddress) ctx.channel().remoteAddress();
   logger.warn(String.format("Attempt hacking from: %s", socketAddr.getAddress().getHostName()));
   ctx.close();
  }

  byte[] buffer = new byte[content.readableBytes()];

  content.getBytes(0, buffer);

  switch (buffer[0]) {
   case C1:
   case C3: {
    switch (buffer[2]) {
     case 1: {
      PMSG_SERVERINFO serverInfo = PMSG_SERVERINFO.deserialize(buffer);

     }
     break;
     case 2: {

     }
    }
   }
   break;
   default: {
    InetSocketAddress socketAddr = (InetSocketAddress) ctx.channel().remoteAddress();
    logger.warn(String.format("Attempt hacking from: %s", socketAddr.getAddress().getHostName()));
    ctx.close();
   }
   break;
  }
 }
}