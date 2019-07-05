package muserver.connectserver.handlers;

import muserver.common.messages.PBMSG_HEAD;
import muserver.common.messages.PBMSG_HEAD2;
import muserver.common.messages.PWMSG_HEAD2;
import muserver.connectserver.configs.ServerListConfigs;
import muserver.connectserver.enums.ServerType;
import muserver.connectserver.exceptions.ConnectServerException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.connectserver.messages.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);
 private final static ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients = new ConcurrentHashMap<>();

 private final Map<Short, ServerListConfigs> serverListConfigsMap;

 public TcpConnectServerHandler(Map<Short, ServerListConfigs> serverListConfigsMap) {
  this.serverListConfigsMap = serverListConfigsMap;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info(String.format("Accepted new connection from: %s", remoteAddress.getAddress().getHostName()));
  }

  clients.put(ctx.channel().id(), ctx);

  PMSG_HANDSHAKE handshake = PMSG_HANDSHAKE.create(PBMSG_HEAD.create((byte) 0xC1, (byte) 4, (byte) 0), (byte) 1);

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

   if (buffer.length < 4) {
    closeConnection(ctx);
    logger.warn(String.format("Invalid buffer length that equals to: %d", buffer.length));
   }

   switch (buffer[0]) {
    case (byte) 0xC1: {
     switch (buffer[2]) {
      case (byte) 0xF4: {
       switch (buffer[3]) {
        case 3: {
         PMSG_SERVER_CODE serverCode = PMSG_SERVER_CODE.deserialize(new ByteArrayInputStream(buffer));

         ServerListConfigs serverListConfigs = serverListConfigsMap.getOrDefault(serverCode.serverCode().shortValue(), null);

         if (serverListConfigs == null) {
          closeConnection(ctx);
          throw new ConnectServerException(String.format("Server code: %d mismatch configuration", serverCode.serverCode()));
         }

         byte sizeOf = (byte) PMSG_SERVER_CONNECTION.sizeOf();

         PMSG_SERVER_CONNECTION serverConnection = PMSG_SERVER_CONNECTION.create(
             PBMSG_HEAD2.create((byte) 0xC1, sizeOf, serverCode.header().headCode(), serverCode.header().subCode()),
             serverListConfigs.serverAddress(),
             serverListConfigs.serverPort().shortValue()
         );

         ctx.writeAndFlush(Unpooled.wrappedBuffer(serverConnection.serialize(new ByteArrayOutputStream())));
        }
        break;
        case 6: {
         PBMSG_HEAD2 header = PBMSG_HEAD2.deserialize(new ByteArrayInputStream(buffer));

         List<PMSG_SERVER> servers = new ArrayList<>();

         for (ServerListConfigs serverListConfigs : serverListConfigsMap.values()) {
          if (serverListConfigs.serverType() == ServerType.VISIBLE) {
           PMSG_GAMESERVER_INFO gameServerInfo = (PMSG_GAMESERVER_INFO) UdpConnectServerHandler.getAbstractPackets().getOrDefault(serverListConfigs.serverCode().shortValue(), null);

           if (gameServerInfo == null) {
            closeConnection(ctx);
            throw new ConnectServerException(String.format("Game server connection has been interrupted. Server code: %d", serverListConfigs.serverCode()));
           }

           servers.add(PMSG_SERVER.create(serverListConfigs.serverCode(), gameServerInfo.percent(), (byte) 0xCC));
          }
         }

         short sizeOf = (short) (PWMSG_HEAD2.sizeOf() + 2 + (servers.size() * 4));

         PMSG_SERVER_LIST serverList = PMSG_SERVER_LIST.create(
             PWMSG_HEAD2.create((byte) 0xC2, sizeOf, header.headCode(), header.subCode()),
             (short) servers.size(),
             servers
         );

         ctx.writeAndFlush(Unpooled.wrappedBuffer(serverList.serialize(new ByteArrayOutputStream())));
        }
        break;
        default: {
         throw new UnsupportedOperationException(String.format("Unsupported sub code: %d", buffer[3]));
        }
       }
      }
      break;
      default: {
       throw new UnsupportedOperationException(String.format("Unsupported head code: %d", buffer[2]));
      }
     }
    }
    break;
    default:
     throw new UnsupportedOperationException(String.format("Unsupported protocol type: %s", buffer[0]));
   }
  } else {
   closeConnection(ctx);
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