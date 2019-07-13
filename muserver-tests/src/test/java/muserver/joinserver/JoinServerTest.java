package muserver.joinserver;

import database.dialect.DatabaseDialect;
import database.dialect.SqlServerDatabaseDialect;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;
import muserver.joinserver.contexts.JoinServerContext;
import muserver.joinserver.handlers.TcpJoinServerHandler;
import muserver.joinserver.messages.SDHP_IDPASS;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

public class JoinServerTest {
 private final static String JDBC_CONNECTION_STRING = "jdbc:sqlserver://192.168.1.139:1433;instanceName=VM139;databaseName=MuOnline;username=sa;password=test";

 private static EmbeddedChannel embeddedChannel;

 @BeforeAll
 public static void beforeAll() throws Exception {
  DatabaseDialect dialect = new SqlServerDatabaseDialect(JDBC_CONNECTION_STRING);

  embeddedChannel = new EmbeddedChannel(new TcpJoinServerHandler(
      new JoinServerContext(dialect)
  ));
 }

 @Test
 public void testJoinIdPassRequest() throws Exception {
  SDHP_IDPASS idPass = SDHP_IDPASS.create(
      PBMSG_HEAD.create(Globals.PMHC_BYTE, (byte) 0x2C, (byte) 1), (byte) 0, (short) 9000, "test", "test", "127.0.0.1"
  );

  embeddedChannel.writeInbound(Unpooled.wrappedBuffer(idPass.serialize(new ByteArrayOutputStream())));

  byte[] buffer = embeddedChannel.readInbound();
 }
}
