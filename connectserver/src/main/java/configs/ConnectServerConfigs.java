package configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ConnectServerConfigs {
 @JsonProperty("listeningPortsConfigs")
 public abstract ListeningPortsConfigs listeningPortsConfigs();

 @JsonProperty("gameServersConfigs")
 public abstract List<ServerListConfigs> gameServersConfigs();

 @JsonCreator
 public static ConnectServerConfigs create(
     @JsonProperty("listeningPortsConfigs") ListeningPortsConfigs listeningPortsConfigs,
     @JsonProperty("gameServersConfigs") List<ServerListConfigs> gameServersConfigs
 ) {
  return builder()
      .listeningPortsConfigs(listeningPortsConfigs)
      .gameServersConfigs(gameServersConfigs)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_ConnectServerConfigs.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder listeningPortsConfigs(ListeningPortsConfigs listeningPortsConfigs);

  public abstract Builder gameServersConfigs(List<ServerListConfigs> gameServersConfigs);

  public abstract ConnectServerConfigs build();
 }
}
