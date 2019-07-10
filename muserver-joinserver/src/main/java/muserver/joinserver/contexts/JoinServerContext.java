package muserver.joinserver.contexts;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;

public class JoinServerContext {
 private final ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients;

 public JoinServerContext() {
  clients = new ConcurrentHashMap<>();
 }

 public ConcurrentHashMap<ChannelId, ChannelHandlerContext> clients() {
  return clients;
 }
}
