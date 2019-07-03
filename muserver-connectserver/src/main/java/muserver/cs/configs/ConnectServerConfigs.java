package muserver.cs.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ConnectServerConfigs extends AbstractConfigs {
 @JsonCreator
 public static ConnectServerConfigs create(
     @JsonProperty("tcpPort") Integer tcpPort,
     @JsonProperty("udpPort") Integer udpPort,
     @JsonProperty("serverList") List<ServerListConfigs> serverListConfigs
 ) {
  return builder()
      .tcpPort(tcpPort)
      .udpPort(udpPort)
      .serverListConfigs(serverListConfigs)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_ConnectServerConfigs.Builder();
 }

 @JsonProperty("tcpPort")
 public abstract Integer tcpPort();

 @JsonProperty("udpPort")
 public abstract Integer udpPort();

 @JsonProperty("serverList")
 public abstract List<ServerListConfigs> serverListConfigs();

 @AutoValue.Builder
 public abstract static class Builder {

  public abstract Builder serverListConfigs(List<ServerListConfigs> serverListConfigs);

  public abstract Builder tcpPort(Integer tcpPort);

  public abstract Builder udpPort(Integer udpPort);

  public abstract ConnectServerConfigs build();
 }
}
