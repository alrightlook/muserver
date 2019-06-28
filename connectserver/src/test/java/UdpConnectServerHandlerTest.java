import configs.ConnectServerConfigs;
import enums.ServerType;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.SocketUtils;
import messages.PMSG_GAMESERVER_STATISTICS;
import messages.PMSG_HEAD;
import org.junit.jupiter.api.Test;
import configs.ServerListConfigs;
import configs.ListeningPortsConfigs;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class UdpConnectServerHandlerTest {
 private final static int GS_PORT = 55901, TCP_PORT = 44405, UDP_PORT = 55557;

 @Test
 public void testUdpConnectServerHandlerValidPacket() throws Exception {
  EmbeddedChannel embeddedChannel = new EmbeddedChannel(new UdpConnectServerHandler(
      ConnectServerConfigs.create(
          ListeningPortsConfigs.create(TCP_PORT, UDP_PORT),
          Arrays.asList(
              ServerListConfigs.create(
                  (short) 19, "GameServer", "localhost", GS_PORT, ServerType.VISIBLE
              )
          )
      )
  ));

  PMSG_GAMESERVER_STATISTICS gsInfo = PMSG_GAMESERVER_STATISTICS.create(
      PMSG_HEAD.create((byte) 0xC1, (byte) 10, (byte) 1),
      (short) 19, (byte) 0, (short) 0, (short) 0, (short) 0, (short) 100
  );

  embeddedChannel.writeInbound(new DatagramPacket(
      Unpooled.wrappedBuffer(gsInfo.serialize(new ByteArrayOutputStream())),
      SocketUtils.socketAddress("localhost", UDP_PORT)
  ));
 }
}