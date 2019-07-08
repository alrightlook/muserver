package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import muserver.common.Globals;
import muserver.common.configs.ServerConfigs;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.messages.PBMSG_HEAD2;
import muserver.common.messages.PWMSG_HEAD2;
import muserver.common.types.ServerType;
import muserver.common.utils.NettyUtils;
import muserver.connectserver.contexts.ConnectServerContext;
import muserver.connectserver.exceptions.ConnectServerException;
import muserver.connectserver.messages.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);
 private final static ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients = new ConcurrentHashMap<>();

 private final ConnectServerContext connectServerContext;

 public TcpConnectServerHandler(ConnectServerContext connectServerContext) {
  this.connectServerContext = connectServerContext;
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
    NettyUtils.closeConnection(ctx);
    logger.warn(String.format("Invalid buffer length that equals to: %d", buffer.length));
   }

   switch (buffer[0]) {
    case Globals.C1_PACKET: {
     switch (buffer[2]) {
      case (byte) 0xF4: {
       switch (buffer[3]) {
        case 3: {
         PMSG_SERVER_CODE serverCode = PMSG_SERVER_CODE.deserialize(new ByteArrayInputStream(buffer));

         ServerConfigs serverConfigs = this.connectServerContext.getServersConfigsMap().getOrDefault(serverCode.serverCode().shortValue(), null);

         if (serverConfigs == null) {
          NettyUtils.closeConnection(ctx);
          throw new ConnectServerException(String.format("Server code: %d mismatch configuration", serverCode.serverCode()));
         }

         byte sizeOf = (byte) PMSG_SERVER_CONNECTION.sizeOf();

         PMSG_SERVER_CONNECTION serverConnection = PMSG_SERVER_CONNECTION.create(
             PBMSG_HEAD2.create((byte) 0xC1, sizeOf, serverCode.header().headCode(), serverCode.header().subCode()),
             serverConfigs.address(),
             serverConfigs.port().shortValue()
         );

         ctx.writeAndFlush(Unpooled.wrappedBuffer(serverConnection.serialize(new ByteArrayOutputStream())));
        }
        break;
        case 6: {
         PBMSG_HEAD2 header = PBMSG_HEAD2.deserialize(new ByteArrayInputStream(buffer));

         List<PMSG_SERVER> servers = new ArrayList<>();

         for (ServerConfigs serverConfigs : this.connectServerContext.getServersConfigsMap().values()) {
          if (serverConfigs.type() == ServerType.VISIBLE) {
           PMSG_GAMESERVER_INFO gameServerInfo = (PMSG_GAMESERVER_INFO) UdpConnectServerHandler.getAbstractPackets().getOrDefault(serverConfigs.code().shortValue(), null);

           if (gameServerInfo == null) {
            NettyUtils.closeConnection(ctx);
            throw new ConnectServerException(String.format("Game server connection has been interrupted. Server code: %d", serverConfigs.code()));
           }

           servers.add(PMSG_SERVER.create(serverConfigs.code(), gameServerInfo.percent(), (byte) 0xCC));
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
   NettyUtils.closeConnection(ctx);
  }
 }
}