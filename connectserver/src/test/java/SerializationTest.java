import com.fasterxml.jackson.databind.ObjectMapper;
import enums.ServerType;
import messages.PMSG_GAMESERVERINFO;
import messages.PMSG_HEAD;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import settings.ConnectServerSettings;
import settings.ListeningPortsSettings;
import settings.GameServerSettings;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class SerializationTest {
 private final static ObjectMapper json = new ObjectMapper();

 @Test
 public void testConnectServerSettingsSerialization() throws Exception {
  int serverCode = 0, serverPort = 55901;
  String serverName = "testServerName", serverAddress = "testServerAddress";
  ServerType serverType = ServerType.VISIBLE;
  ConnectServerSettings expectedConnectServerSettings = ConnectServerSettings.create(
      ListeningPortsSettings.create(44405, 55557),
      Arrays.asList(
          GameServerSettings.create(
              serverCode,
              serverName,
              serverAddress,
              serverPort,
              serverType
          )
      )
  );
  String jsonString = json.writeValueAsString(expectedConnectServerSettings);
  ConnectServerSettings actualConnectServerSettings = json.readValue(jsonString, ConnectServerSettings.class);
  Assertions.assertEquals(expectedConnectServerSettings, actualConnectServerSettings);
 }

 @Test
 public void testGameServerSettingsSerialization() throws Exception {
  int serverCode = 0, serverPort = 55901;
  String serverName = "testServerName", serverAddress = "testServerAddress";
  ServerType serverType = ServerType.VISIBLE;
  GameServerSettings expectedGameServerSettings = GameServerSettings.create(
      serverCode,
      serverName,
      serverAddress,
      serverPort,
      serverType
  );
  String jsonString = json.writeValueAsString(expectedGameServerSettings);
  GameServerSettings actualGameServerSettings = json.readValue(jsonString, GameServerSettings.class);
  Assertions.assertEquals(expectedGameServerSettings, actualGameServerSettings);
 }

 @Test
 public void testListeningPortsSettingsSerialization() throws Exception {
  ListeningPortsSettings expectedListeningPortsSettings = ListeningPortsSettings.create(44405, 55557);
  String jsonString = json.writeValueAsString(expectedListeningPortsSettings);
  ListeningPortsSettings actualListeningPortsSettings = json.readValue(jsonString, ListeningPortsSettings.class);
  Assertions.assertEquals(expectedListeningPortsSettings, actualListeningPortsSettings);
 }
}