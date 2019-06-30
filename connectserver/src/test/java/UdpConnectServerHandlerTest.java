import com.google.common.collect.ImmutableMap;
import configs.ConnectServerConfigs;
import enums.ServerType;
import handlers.UdpConnectServerHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.SocketUtils;
import messages.PMSG_GAMESERVER_INFO;
import messages.PMSG_HEAD;
import org.junit.jupiter.api.Test;
import configs.ServerListConfigs;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class UdpConnectServerHandlerTest {
 private final static String HOSTNAME = "localhost";
 private final static Integer GS_PORT = 55901, TCP_PORT = 44405, UDP_PORT = 55557;

 @Test
 public void testUdpConnectServerHandlerGameServerStatistics() throws Exception {
  EmbeddedChannel embeddedChannel = new EmbeddedChannel(
      new UdpConnectServerHandler(ImmutableMap.of((short) 0, ServerListConfigs.create((short) 0, "GameServer", HOSTNAME, GS_PORT, ServerType.VISIBLE)))
  );

  PMSG_GAMESERVER_INFO gsInfo = PMSG_GAMESERVER_INFO.create(
      PMSG_HEAD.create((byte) 0xC1, (byte) 10, (byte) 1),
      (short) 19, (byte) 0, (short) 0, (short) 0, (short) 0, (short) 100
  );

  embeddedChannel.writeInbound(new DatagramPacket(
      Unpooled.wrappedBuffer(gsInfo.serialize(new ByteArrayOutputStream())),
      SocketUtils.socketAddress(HOSTNAME, UDP_PORT)
  ));
 }
}