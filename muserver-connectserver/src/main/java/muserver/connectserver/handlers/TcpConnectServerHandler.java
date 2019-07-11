package muserver.connectserver.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
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

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);

 private final ConnectServerContext connectServerContext;

 public TcpConnectServerHandler(ConnectServerContext connectServerContext) {
  this.connectServerContext = connectServerContext;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info("Connection with: {} is accepted", remoteAddress.getAddress().getHostName());
  }

  connectServerContext.clients().put(ctx.channel().id(), ctx);

  PMSG_HANDSHAKE handshake = PMSG_HANDSHAKE.create(PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) PMSG_HANDSHAKE.sizeOf(), (byte) 0), (byte) 1);

  byte[] buffer = handshake.serialize(new ByteArrayOutputStream());

  ctx.writeAndFlush(Unpooled.wrappedBuffer(buffer));
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info("Connection with: {} is interrupted", remoteAddress.getAddress().getHostName());
  }

  connectServerContext.clients().remove(ctx.channel().id());
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
  if (byteBuf.readableBytes() < 4) {
   logger.warn("Invalid buffer length: {}", byteBuf.readableBytes());
   NettyUtils.closeConnection(ctx);
  }

  byte[] buffer = new byte[byteBuf.readableBytes()];

  byteBuf.getBytes(0, buffer);

  if (buffer[0] != Globals.PMHC_BYTE) {
   logger.warn("Invalid protocol type: {}", buffer[0]);
   NettyUtils.closeConnection(ctx);
  }

  switch (buffer[2]) {
   case (byte) 0xF4: {
    switch (buffer[3]) {
     case 3: {
      PMSG_REQ_SERVER_INFO serverInfoRequest = PMSG_REQ_SERVER_INFO.deserialize(new ByteArrayInputStream(buffer));

      ServerConfigs serverConfigs = this.connectServerContext.serversConfigsMap().getOrDefault(serverInfoRequest.serverCode().shortValue(), null);

      if (serverConfigs == null) {
       NettyUtils.closeConnection(ctx);
       throw new ConnectServerException(String.format("Server code: %d mismatch configuration", serverInfoRequest.serverCode()));
      }

      PMSG_ANS_SERVER_INFO serverConnection = PMSG_ANS_SERVER_INFO.create(
          PBMSG_HEAD2.create(Globals.PMHC_BYTE, (byte) PMSG_ANS_SERVER_INFO.sizeOf(), serverInfoRequest.header().headCode(), serverInfoRequest.header().subCode()),
          serverConfigs.ip(),
          serverConfigs.port().shortValue()
      );

      ctx.writeAndFlush(Unpooled.wrappedBuffer(serverConnection.serialize(new ByteArrayOutputStream())));
     }
     break;

     case 6: {
      PBMSG_HEAD2 header = PBMSG_HEAD2.deserialize(new ByteArrayInputStream(buffer));

      List<PMSG_SERVER> servers = new ArrayList<>();

      for (ServerConfigs serverConfigs : this.connectServerContext.serversConfigsMap().values()) {
       if (serverConfigs.type() == ServerType.VISIBLE) {
        //todo: Request players count from GS
        servers.add(PMSG_SERVER.create(serverConfigs.code(), (byte) 0, (byte) 0xCC));
       }
      }

      short sizeOf = (short) (PMSG_SERVERLIST.sizeOf() + (servers.size() * PMSG_SERVER.sizeOf()));

      PMSG_SERVERLIST serverList = PMSG_SERVERLIST.create(
          PWMSG_HEAD2.create(Globals.PMHC_WORD, sizeOf, header.headCode(), header.subCode()),
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
    throw new UnsupportedOperationException(String.format("Unsupported header code: %d", buffer[2]));
   }
  }
 }
}