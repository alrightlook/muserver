package muserver.common.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CommonConfigs extends AbstractConfigs {
 public static Builder builder() {
  return new AutoValue_CommonConfigs.Builder();
 }

 @JsonCreator
 public static CommonConfigs create(
         @JsonProperty("connectServer") ConnectServerConfigs connectServer,
         @JsonProperty("joinServer") JoinServerConfigs joinServer,
         @JsonProperty("dataServer") DataServerConfigs dataServer,
         @JsonProperty("gameServer") GameServerConfigs gameServer
 ) {
  return builder()
          .connectServer(connectServer)
          .joinServer(joinServer)
          .dataServer(dataServer)
          .gameServer(gameServer)
          .build();
 }

 @JsonProperty("connectServer")
 public abstract ConnectServerConfigs connectServer();

 @JsonProperty("joinServer")
 public abstract JoinServerConfigs joinServer();

 @JsonProperty("dataServer")
 public abstract DataServerConfigs dataServer();

 @JsonProperty("gameServer")
 public abstract GameServerConfigs gameServer();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder joinServer(JoinServerConfigs joinServer);

  public abstract Builder connectServer(ConnectServerConfigs connectServer);

  public abstract Builder gameServer(GameServerConfigs gameServer);

  public abstract Builder dataServer(DataServerConfigs dataServer);

  public abstract CommonConfigs build();
 }
}