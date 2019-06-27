import enums.ServerType;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.SocketUtils;
import messages.PMSG_GAMESERVERINFO;
import messages.PMSG_HEAD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import settings.ConnectServerSettings;
import settings.GameServerSettings;
import settings.ListeningPortsSettings;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UdpConnectServerHandlerTest {
 private final static int GS_PORT = 55901, TCP_PORT = 44405, UDP_PORT = 55557;

 @Test
 public void testUdpConnectServerHandlerValidPacket() throws Exception {
  EmbeddedChannel embeddedChannel = new EmbeddedChannel(new UdpConnectServerHandler(
      ConnectServerSettings.create(
          ListeningPortsSettings.create(TCP_PORT, UDP_PORT),
          Arrays.asList(
              GameServerSettings.create(
                  (short) 19, "GameServer", "localhost", GS_PORT, ServerType.VISIBLE
              )
          )
      )
  ));

  PMSG_GAMESERVERINFO gsInfo = PMSG_GAMESERVERINFO.create(
      PMSG_HEAD.create((byte) 0xC1, (byte) 10, (byte) 1),
      (short) 19, (byte) 0, (short) 0, (short) 0, (short) 0, (short) 100
  );

  embeddedChannel.writeInbound(new DatagramPacket(
      Unpooled.wrappedBuffer(gsInfo.serialize(new ByteArrayOutputStream())),
      SocketUtils.socketAddress("localhost", UDP_PORT)
  ));
 }
}