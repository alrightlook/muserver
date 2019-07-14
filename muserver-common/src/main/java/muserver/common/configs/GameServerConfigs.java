package muserver.common.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GameServerConfigs extends AbstractConfigs {
 @JsonCreator
 public static GameServerConfigs create(
     @JsonProperty("hostname")String hostname,
     @JsonProperty("tcpPort") Integer tcpPort
 ) {
  return builder()
      .hostname(hostname)
      .tcpPort(tcpPort)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_GameServerConfigs.Builder();
 }

 @JsonProperty("hostname")
 public abstract String hostname();

 @JsonProperty("tcpPort")
 public abstract Integer tcpPort();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder hostname(String hostname);

  public abstract Builder tcpPort(Integer tcpPort);

  public abstract GameServerConfigs build();
 }
}
