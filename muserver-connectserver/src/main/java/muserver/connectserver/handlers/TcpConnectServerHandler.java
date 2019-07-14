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
import muserver.common.utils.HexUtils;
import muserver.connectserver.contexts.ConnectServerContext;
import muserver.connectserver.exceptions.TcpConnectServerHandlerException;
import muserver.connectserver.messages.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection accepted: {}", ctx.channel().remoteAddress().toString());
  }

  PMSG_HANDSHAKE handshake = PMSG_HANDSHAKE.create(PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) PMSG_HANDSHAKE.sizeOf(), (byte) 0), (byte) 1);

  byte[] buffer = handshake.serialize(new ByteArrayOutputStream());

  ctx.writeAndFlush(Unpooled.wrappedBuffer(buffer));
 }

 @Override
 public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  if (ctx.channel().remoteAddress() != null) {
   logger.info("Connection interrupted: {}", ctx.channel().remoteAddress().toString());
  }
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
   throw new TcpConnectServerHandlerException(String.format("Invalid buffer length: %d", byteBuf.readableBytes()));
  }

  byte[] buffer = new byte[byteBuf.readableBytes()];

  byteBuf.getBytes(0, buffer);

  if (buffer[0] != Globals.PMHC_BYTE) {
   throw new TcpConnectServerHandlerException(String.format("Invalid protocol type: %d", buffer[0]));
  }

  logger.info(HexUtils.toString(buffer));

  switch (buffer[2]) {
   case (byte) 0xF4: {
    switch (buffer[3]) {
     case 3: {
      PMSG_REQ_SERVER_INFO requestServerInfo = PMSG_REQ_SERVER_INFO.deserialize(new ByteArrayInputStream(buffer));

      ServerConfigs serverConfigs = this.connectServerContext.serversConfigsMap().getOrDefault(requestServerInfo.serverCode().shortValue(), null);

      if (serverConfigs == null) {
       throw new TcpConnectServerHandlerException( String.format("Server id: %d mismatch configuration", requestServerInfo.serverCode()));
      }

      PMSG_RESP_SERVER_INFO responseServerInfo = PMSG_RESP_SERVER_INFO.create(
          PBMSG_HEAD2.create(Globals.PMHC_BYTE, (byte) PMSG_RESP_SERVER_INFO.sizeOf(), requestServerInfo.header().headCode(), requestServerInfo.header().subCode()),
          serverConfigs.ip(),
          serverConfigs.port().shortValue()
      );

      ctx.writeAndFlush(Unpooled.wrappedBuffer(responseServerInfo.serialize(new ByteArrayOutputStream())));
     }
     break;

     case 6: {
      PBMSG_HEAD2 header = PBMSG_HEAD2.deserialize(new ByteArrayInputStream(buffer));

      List<PMSG_SERVER> servers = new ArrayList<>();

      for (ServerConfigs serverConfigs : this.connectServerContext.serversConfigsMap().values()) {
       if (serverConfigs.type() == ServerType.VISIBLE) {
        //todo: Request players count from GS
        servers.add(PMSG_SERVER.create(serverConfigs.id(), (byte) 0, (byte) 0xCC));
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
      throw new TcpConnectServerHandlerException(String.format("Unsupported subcode: %d", buffer[3]));
     }
    }
   }
   break;

   default: {
    throw new TcpConnectServerHandlerException(String.format("Unsupported headcode: %d", buffer[2]));
   }
  }
 }
}