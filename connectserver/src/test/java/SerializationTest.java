import com.fasterxml.jackson.databind.ObjectMapper;
import configs.ConnectServerConfigs;
import configs.ListeningPortsConfigs;
import configs.ServerListConfigs;
import enums.ServerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SerializationTest {
 private final static ObjectMapper json = new ObjectMapper();

 @Test
 public void testGameServerSettingsSerialization() throws Exception {
  int serverCode = 0, serverPort = 55901;
  String serverName = "testServerName", serverAddress = "testServerAddress";
  ServerType serverType = ServerType.VISIBLE;
  ServerListConfigs expectedServerListConfigs = ServerListConfigs.create(
      (short) serverCode,
      serverName,
      serverAddress,
      serverPort,
      serverType
  );
  String jsonString = json.writeValueAsString(expectedServerListConfigs);
  ServerListConfigs actualServerListConfigs = json.readValue(jsonString, ServerListConfigs.class);
  Assertions.assertEquals(expectedServerListConfigs, actualServerListConfigs);
 }

 @Test
 public void testConnectServerSettingsSerialization() throws Exception {
  int serverCode = 0, serverPort = 55901;
  String serverName = "testServerName", serverAddress = "testServerAddress";
  ServerType serverType = ServerType.VISIBLE;
  ConnectServerConfigs expectedConnectServerConfigs = ConnectServerConfigs.create(
      ListeningPortsConfigs.create(44405, 55557),
      Arrays.asList(
          ServerListConfigs.create(
              (short) serverCode,
              serverName,
              serverAddress,
              serverPort,
              serverType
          )
      )
  );
  String jsonString = json.writeValueAsString(expectedConnectServerConfigs);
  ConnectServerConfigs actualConnectServerConfigs = json.readValue(jsonString, ConnectServerConfigs.class);
  Assertions.assertEquals(expectedConnectServerConfigs, actualConnectServerConfigs);
 }

 @Test
 public void testListeningPortsSettingsSerialization() throws Exception {
  ListeningPortsConfigs expectedListeningPortsConfigs = ListeningPortsConfigs.create(44405, 55557);
  String jsonString = json.writeValueAsString(expectedListeningPortsConfigs);
  ListeningPortsConfigs actualListeningPortsConfigs = json.readValue(jsonString, ListeningPortsConfigs.class);
  Assertions.assertEquals(expectedListeningPortsConfigs, actualListeningPortsConfigs);
 }
}