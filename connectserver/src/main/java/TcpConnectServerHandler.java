import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import messages.PMSG_HEAD;
import messages.PWMSG_HEAD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.ConnectServerSettings;

import java.io.ByteArrayInputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<byte[]> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);
 private final static ConcurrentHashMap<ChannelId, Client> clients = new ConcurrentHashMap<>();

 public TcpConnectServerHandler(ConnectServerSettings connectServerSettings) {
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  ctx.flush();
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  clients.put(ctx.channel().id(), Client.create(clients.size()));
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

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, byte[] buffer) throws Exception {
  if (buffer.length > 0) {
   switch (buffer[0]) {
    case (byte) 0xC1: {
     PMSG_HEAD header = PMSG_HEAD.deserialize(new ByteArrayInputStream(buffer));
     handleProtocol(header.headCode(), buffer[3]);
    }
    break;
    case (byte) 0xC2: {
     PWMSG_HEAD header = PWMSG_HEAD.deserialize(new ByteArrayInputStream(buffer));
     handleProtocol(header.headCode(), buffer[4]);
    }
    break;
   }
  } else {
   closeImmediately(ctx);
  }
 }

 private void handleProtocol(byte headCode, byte subCode) {
  switch (headCode) {
   case 5: {
    //todo:
   }
   break;

   case 6: {
    //todo:
   }
   break;

   case (byte) 0xF4: {
    switch (subCode) {
     case 6: {
      //todo:
     }
     break;
     case 7: {
      //todo:
     }
     break;
    }
   }
   break;
  }
 }

 private void closeImmediately(ChannelHandlerContext ctx) {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
  if (remoteAddress != null) {
   logger.warn(String.format("Hacking attempt from: %s", remoteAddress.getAddress().getHostName()));
   ctx.close();
  }
 }
}
