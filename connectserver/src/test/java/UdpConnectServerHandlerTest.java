import enums.ServerType;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.SocketUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import settings.ConnectServerSettings;
import settings.GameServerSettings;
import settings.ListeningPortsSettings;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class UdpConnectServerHandlerTest {
    private final static int GS_PORT = 55901, TCP_PORT = 44405, UDP_PORT = 55557;

    @Test
    public void testUdpConnectServerHandler() throws Exception {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new UdpConnectServerHandler(
            ConnectServerSettings.create(
                ListeningPortsSettings.create(TCP_PORT, UDP_PORT),
                Arrays.asList(
                    GameServerSettings.create(
                        1, "GameServer", "localhost", GS_PORT, ServerType.VISIBLE
                    )
                )
            )
        ));

        embeddedChannel.writeInbound(new DatagramPacket(
            Unpooled.wrappedBuffer(new byte[] {(byte) 0xc1, 0x08, 02, 00, 00, 00, 00, 00}),
            SocketUtils.socketAddress("localhost", UDP_PORT)
        ));

        embeddedChannel.writeInbound(new DatagramPacket(
            Unpooled.wrappedBuffer(new byte[] {(byte) 0xc1, 0x10, 01, 00, 01, 00, 00, 00, 00, 00, 00, 00, 00, 00, 0x64, 00}),
            SocketUtils.socketAddress("localhost", UDP_PORT)
        ));

        Thread.sleep(1000 * 60);
    }
}