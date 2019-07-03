package muserver.connectserver.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import muserver.connectserver.enums.ServerType;

@AutoValue
public abstract class ServerListConfigs extends AbstractConfigs {
 @JsonCreator
 public static ServerListConfigs create(
     @JsonProperty("serverCode") Short serverCode,
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
  return new AutoValue_ServerListConfigs.Builder();
 }

 @JsonProperty("serverCode")
 public abstract Short serverCode();

 @JsonProperty("serverName")
 public abstract String serverName();

 @JsonProperty("serverAddress")
 public abstract String serverAddress();

 @JsonProperty("serverPort")
 public abstract Integer serverPort();

 @JsonProperty("serverType")
 public abstract ServerType serverType();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder serverCode(Short serverCode);

  public abstract Builder serverName(String serverName);

  public abstract Builder serverAddress(String serverAddress);

  public abstract Builder serverPort(Integer serverPort);

  public abstract Builder serverType(ServerType serverType);

  public abstract ServerListConfigs build();
 }
}
