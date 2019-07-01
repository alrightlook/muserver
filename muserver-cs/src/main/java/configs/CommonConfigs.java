package configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CommonConfigs extends AbstractConfigs {
 @JsonCreator
 public static CommonConfigs create(
     @JsonProperty("logging") LoggingConfigs logging,
     @JsonProperty("connectServer") ConnectServerConfigs connectServer
 ) {
  return builder()
      .logging(logging)
      .connectServer(connectServer)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_CommonConfigs.Builder();
 }

 @JsonProperty("logging")
 public abstract LoggingConfigs logging();

 @JsonProperty("connectServer")
 public abstract ConnectServerConfigs connectServer();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder logging(LoggingConfigs loggingConfigs);

  public abstract Builder connectServer(ConnectServerConfigs connectServer);

  public abstract CommonConfigs build();
 }
}
