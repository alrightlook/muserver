import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageDecoder;
import messages.PMSG_HANDSHAKE;
import messages.PMSG_HEAD;
import messages.PMSG_HEAD2;
import messages.PWMSG_HEAD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import settings.ConnectServerSettings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);
 private final static ConcurrentHashMap<ChannelId, Client> clients = new ConcurrentHashMap<>();

 public TcpConnectServerHandler(ConnectServerSettings connectServerSettings) {
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info(String.format("Accepted new connection from: %s", remoteAddress.getAddress().getHostName()));
  }

  clients.put(ctx.channel().id(), Client.create(clients.size()));

  PMSG_HANDSHAKE handshake = PMSG_HANDSHAKE.create(PMSG_HEAD.create((byte) 0xC1, (byte) 4, (byte) 0), (byte) 1);

  byte[] buffer = handshake.serialize(new ByteArrayOutputStream());

  ctx.writeAndFlush(Unpooled.wrappedBuffer(buffer));
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info(String.format("Connection from: %s has been interrupted", remoteAddress.getAddress().getHostName()));
  }

  clients.remove(ctx.channel().id());
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
  logger.error(cause.getMessage(), cause);
  ctx.close();
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
  ctx.flush();
 }

 @Override
 protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
  if (byteBuf.readableBytes() > 0) {
   byte[] buffer = new byte[byteBuf.readableBytes()];
   byteBuf.getBytes(0, buffer);
   PMSG_HEAD2 header = PMSG_HEAD2.deserialize(new ByteArrayInputStream(buffer));
   switch (header.type()) {
    case (byte) 0xC1: {
     switch (header.headCode()) {
      case (byte) 0xF4: {
       switch (header.subCode()) {
        case 3: {
         //todo: server info
        }
        break;
        case 6: {
         //todo: server list
        }
        break;
        default:
         throw new UnsupportedOperationException(String.format("Unsupported sub code: %d", header.subCode()));
       }
      }
      break;
      default:
       throw new UnsupportedOperationException(String.format("Unsupported head code: %d", header.headCode()));
     }
    }
    break;
    default:
     throw new UnsupportedOperationException(String.format("Unsupported protocol type: %d", header.type()));
   }
  } else {
   closeConnection(ctx);
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

 private void closeConnection(ChannelHandlerContext ctx) {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
  if (remoteAddress != null) {
   logger.warn(String.format("Hacking attempt from: %s", remoteAddress.getAddress().getHostName()));
   ctx.close();
  }
 }
}
