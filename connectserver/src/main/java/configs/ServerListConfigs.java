package configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import enums.ServerType;

@AutoValue
public abstract class ServerListConfigs extends AbstractConfigs {
 @JsonProperty("serverCode")
 public abstract short serverCode();

 @JsonProperty("serverName")
 public abstract String serverName();

 @JsonProperty("serverAddress")
 public abstract String serverAddress();

 @JsonProperty("serverPort")
 public abstract int serverPort();

 @JsonProperty("serverType")
 public abstract ServerType serverType();

 @JsonCreator
 public static ServerListConfigs create(
     @JsonProperty("serverCode") short serverCode,
     @JsonProperty("serverName") String serverName,
     @JsonProperty("serverAddress") String serverAddress,
     @JsonProperty("serverPort") int serverPort,
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
  return new AutoValue_ServerListConfigs.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder serverCode(short serverCode);

  public abstract Builder serverName(String serverName);

  public abstract Builder serverAddress(String serverAddress);

  public abstract Builder serverPort(int serverPort);

  public abstract Builder serverType(ServerType serverType);

  public abstract ServerListConfigs build();
 }
}
