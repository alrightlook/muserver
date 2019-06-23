package settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ConnectServerSettings {
 @JsonProperty("listeningPorts")
 public abstract ListeningPortsSettings listeningPorts();

 @JsonProperty("gameServers")
 public abstract List<GameServerSettings> gameServers();

 @JsonCreator
 public static ConnectServerSettings create(
     @JsonProperty("listeningPorts") ListeningPortsSettings listeningPorts,
     @JsonProperty("gameServers") List<GameServerSettings> gameServers
 ) {
  return builder()
      .listeningPorts(listeningPorts)
      .gameServers(gameServers)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_ConnectServerSettings.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder listeningPorts(ListeningPortsSettings listeningPorts);

  public abstract Builder gameServers(List<GameServerSettings> gameServers);

  public abstract ConnectServerSettings build();
 }
}
