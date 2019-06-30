import com.google.common.collect.ImmutableMap;
import configs.ConnectServerConfigs;
import configs.ServerListConfigs;
import enums.ServerType;
import handlers.TcpConnectServerHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.DatagramPacket;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.Arrays;

public class TcpConnectServerHandlerTest {
 private final static int GS_PORT = 55901, TCP_PORT = 44405, UDP_PORT = 55557;
 private final static String HOSTNAME = "localhost";

 @Test
 public void testTcpConnectServerHandlerServerInfo() throws Exception {
  EmbeddedChannel embeddedChannel = new EmbeddedChannel(new TcpConnectServerHandler(
      ImmutableMap.of((short) 0, ServerListConfigs.create((short) 0, "GameServer", HOSTNAME, GS_PORT, ServerType.VISIBLE))
  ));

  embeddedChannel.writeInbound(Unpooled.wrappedBuffer(new byte[0]));
 }

 @Test
 public void testTcpConnectServerHandlerServerList() throws Exception {
  EmbeddedChannel embeddedChannel = new EmbeddedChannel(new TcpConnectServerHandler(
      ImmutableMap.of((short) 0, ServerListConfigs.create((short) 0, "GameServer", HOSTNAME, GS_PORT, ServerType.VISIBLE))
  ));

  embeddedChannel.writeInbound(new DatagramPacket(Unpooled.wrappedBuffer(new byte[]{(byte) 1}), InetSocketAddress.createUnresolved("0.0.0.0", UDP_PORT)));
 }
}