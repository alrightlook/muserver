import configs.ConnectServerConfigs;
import configs.ServerListConfigs;
import enums.PacketType;
import enums.ServerType;
import exceptions.ConnectServerException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TcpConnectServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
 private final static Logger logger = LogManager.getLogger(TcpConnectServerHandler.class);
 private final static Map<Short, ServerListConfigs> serverListConfigsMap = new HashMap<>();
 private final static ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients = new ConcurrentHashMap<>();

 public TcpConnectServerHandler(ConnectServerConfigs connectServerConfigs) {
  Map<Short, List<ServerListConfigs>> serverListConfigsGroupingBy = connectServerConfigs.gameServersConfigs().stream().collect(Collectors.groupingBy(x -> x.serverCode()));
  for (Map.Entry<Short, List<ServerListConfigs>> entry : serverListConfigsGroupingBy.entrySet()) {
   serverListConfigsMap.put(entry.getKey(), entry.getValue().get(0));
  }
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
   switch (buffer[0]) {
    case PacketType.C1: {
     switch (buffer[2]) {
      case (byte) 0xF4: {
       switch (buffer[3]) {
        case 3: {
         PMSG_SERVER_CODE serverCode = PMSG_SERVER_CODE.deserialize(new ByteArrayInputStream(buffer));

         ServerListConfigs serverListConfigs = serverListConfigsMap.getOrDefault(serverCode.serverCode().shortValue(), null);

         if (serverListConfigs == null) {
          closeConnection(ctx);
          throw new ConnectServerException(String.format("Server code %d not found in the server list configs", serverCode.serverCode()));
         }

         byte packetSize = (byte) PMSG_SERVER_CONNECTION.sizeOf();

         PMSG_SERVER_CONNECTION serverConnection = PMSG_SERVER_CONNECTION.create(
             PMSG_HEAD2.create(PacketType.C1, packetSize, serverCode.header().headCode(), serverCode.header().subCode()),
             serverListConfigs.serverAddress(),
             (short) serverListConfigs.serverPort()
         );

         byte[] packetBytes = serverConnection.serialize(new ByteArrayOutputStream());

         ctx.writeAndFlush(Unpooled.wrappedBuffer(packetBytes));
        }
        break;
        case 6: {
         PMSG_HEAD2 header = PMSG_HEAD2.deserialize(new ByteArrayInputStream(buffer));

         List<PMSG_SERVER> servers = new ArrayList<>();

         for (ServerListConfigs serverListConfigs : serverListConfigsMap.values()) {
          if (serverListConfigs.serverType() == ServerType.VISIBLE) {
           servers.add(PMSG_SERVER.create(serverListConfigs.serverCode(), (byte) 0, (byte) 0xCC));
          }
         }

         short packetSize = (short) (PWMSG_HEAD2.sizeOf() + 2 + (servers.size() * 4));

         PMSG_SERVER_LIST serverList = PMSG_SERVER_LIST.create(
             PWMSG_HEAD2.create(PacketType.C2, packetSize, header.headCode(), header.subCode()),
             (short) servers.size(),
             servers
         );

         byte[] packetBytes = serverList.serialize(new ByteArrayOutputStream());

         ctx.writeAndFlush(Unpooled.wrappedBuffer(packetBytes));
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
