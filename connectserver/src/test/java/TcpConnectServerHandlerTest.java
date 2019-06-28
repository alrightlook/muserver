import configs.ConnectServerConfigs;
import configs.ListeningPortsConfigs;
import configs.ServerListConfigs;
import enums.ServerType;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TcpConnectServerHandlerTest {
 private final static int GS_PORT = 55901, TCP_PORT = 44405, UDP_PORT = 55557;
 private final static String HOSTNAME = "localhost";

 @Test
 public void testTcpConnectServerHandlerServerInfo() throws Exception {
  EmbeddedChannel embeddedChannel = new EmbeddedChannel(new TcpConnectServerHandler(
      ConnectServerConfigs.create(
          ListeningPortsConfigs.create(TCP_PORT, UDP_PORT),
          Arrays.asList(
              ServerListConfigs.create(
                  (short) 0, "GameServer", HOSTNAME, GS_PORT, ServerType.VISIBLE
              )
          )
      )
  ));

  embeddedChannel.writeInbound(Unpooled.wrappedBuffer(new byte[0]));
 }

 @Test
 public void testTcpConnectServerHandlerServerList() throws Exception {
  EmbeddedChannel embeddedChannel = new EmbeddedChannel(new TcpConnectServerHandler(
      ConnectServerConfigs.create(
          ListeningPortsConfigs.create(TCP_PORT, UDP_PORT),
          Arrays.asList(
              ServerListConfigs.create(
                  (short) 0, "GameServer", HOSTNAME, GS_PORT, ServerType.VISIBLE
              )
          )
      )
  ));

  embeddedChannel.writeInbound(Unpooled.wrappedBuffer(new byte[0]));
 }
}