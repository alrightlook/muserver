import configs.ConnectServerConfigs;
import configs.ServerListConfigs;
import enums.PacketType;
import enums.ServerType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import messages.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);
 private final static ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients = new ConcurrentHashMap<>();
 private final ConnectServerConfigs connectServerConfigs;

 public TcpConnectServerHandler(ConnectServerConfigs connectServerConfigs) {
  this.connectServerConfigs = connectServerConfigs;
 }

 @Override
 public void channelActive(ChannelHandlerContext ctx) throws Exception {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

  if (remoteAddress != null) {
   logger.info(String.format("Accepted new connection from: %s", remoteAddress.getAddress().getHostName()));
  }

  clients.put(ctx.channel().id(), ctx);

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
   handleProtocol(ctx, header);
  } else {
   closeConnection(ctx);
  }
 }


 private void handleProtocol(ChannelHandlerContext ctx, PMSG_HEAD2 header) throws IOException {
  switch (header.type()) {
   case (byte) PacketType.C1: {
    switch (header.headCode()) {
     case (byte) 0xF4: {
      switch (header.subCode()) {
       case 3: {
        //todo:
       }
       break;
       case 6: {
        List<PMSG_SERVER> servers = new ArrayList<>();

        for (ServerListConfigs serverListConfigs : connectServerConfigs.gameServersConfigs()) {
         if (serverListConfigs.serverType() == ServerType.VISIBLE) {
          servers.add(PMSG_SERVER.create(serverListConfigs.serverCode(), (byte) 20, (byte) 0xCC));
         }
        }

        short packetSize = (short) (PWMSG_HEAD2.sizeOf() + 2 + (servers.size() * 4));

        PMSG_SERVERLIST serverList = PMSG_SERVERLIST.create(
            PWMSG_HEAD2.create(PacketType.C2, packetSize, header.headCode(), header.subCode()),
            (short) servers.size(),
            servers
        );

        byte[] buffer = serverList.serialize(new ByteArrayOutputStream());

        ctx.writeAndFlush(Unpooled.wrappedBuffer(buffer));
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
 }

 private void closeConnection(ChannelHandlerContext ctx) {
  InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
  if (remoteAddress != null) {
   logger.warn(String.format("Hacking attempt from: %s", remoteAddress.getAddress().getHostName()));
   ctx.close();
  }
 }
}
