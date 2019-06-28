package configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ListeningPortsConfigs {
 @JsonProperty("tcpPort")
 public abstract Integer tcpPort();

 @JsonProperty("udpPort")
 public abstract Integer udpPort();

 @JsonCreator
 public static ListeningPortsConfigs create(
     @JsonProperty("tcpPort") Integer tcpPort,
     @JsonProperty("udpPort") Integer udpPort
 ) {
  return builder()
      .tcpPort(tcpPort)
      .udpPort(udpPort)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_ListeningPortsConfigs.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder tcpPort(Integer tcpPort);

  public abstract Builder udpPort(Integer udpPort);

  public abstract ListeningPortsConfigs build();
 }
}
