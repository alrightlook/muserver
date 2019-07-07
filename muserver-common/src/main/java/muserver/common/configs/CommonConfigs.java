package muserver.common.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CommonConfigs extends AbstractConfigs {

 public static Builder builder() {
  return new AutoValue_CommonConfigs.Builder();
 }

 @JsonProperty("logging")
 public abstract LoggingConfigs logging();

 @JsonProperty("joinServer")
 public abstract JoinServerConfigs joinServer();

 @JsonProperty("connectServer")
 public abstract ConnectServerConfigs connectServer();

 @JsonCreator
 public static CommonConfigs create(
     @JsonProperty("logging") LoggingConfigs logging,
     @JsonProperty("joinServer") JoinServerConfigs joinServer,
     @JsonProperty("connectServer") ConnectServerConfigs connectServer
 ) {
  return builder()
      .logging(logging)
      .joinServer(joinServer)
      .connectServer(connectServer)
      .build();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder logging(LoggingConfigs loggingConfigs);

  public abstract Builder connectServer(ConnectServerConfigs connectServer);

  public abstract Builder joinServer(JoinServerConfigs joinServer);

  public abstract CommonConfigs build();
 }
}
