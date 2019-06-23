package settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import enums.ServerType;

@AutoValue
public abstract class GameServerSettings {
 @JsonProperty("serverCode")
 public abstract Integer serverCode();

 @JsonProperty("serverName")
 public abstract String serverName();

 @JsonProperty("serverAddress")
 public abstract String serverAddress();

 @JsonProperty("serverPort")
 public abstract Integer serverPort();

 @JsonProperty("serverType")
 public abstract ServerType serverType();

 @JsonCreator
 public static GameServerSettings create(
     @JsonProperty("serverCode") Integer serverCode,
     @JsonProperty("serverName") String serverName,
     @JsonProperty("serverAddress") String serverAddress,
     @JsonProperty("serverPort") Integer serverPort,
     @JsonProperty("serverType") ServerType serverType
 ) {
  return builder()
      .serverCode(serverCode)
      .serverName(serverName)
      .serverAddress(serverAddress)
      .serverPort(serverPort)
      .serverType(serverType)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_GameServerSettings.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder serverCode(Integer serverCode);

  public abstract Builder serverName(String serverName);

  public abstract Builder serverAddress(String serverAddress);

  public abstract Builder serverPort(Integer serverPort);

  public abstract Builder serverType(ServerType serverType);

  public abstract GameServerSettings build();
 }
}
