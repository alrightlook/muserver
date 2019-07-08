package muserver.common.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class JoinServerConfigs extends AbstractConfigs {
 @JsonProperty("hostname")
 public abstract String hostname();

 @JsonProperty("tcpPort")
 public abstract Integer tcpPort();

 @JsonProperty("connectionString")
 public abstract String connectionString();

 @JsonCreator
 public static JoinServerConfigs create(
         @JsonProperty("hostname") String hostname,
         @JsonProperty("tcpPort") Integer tcpPort,
         @JsonProperty("connectionString") String connectionString
 ) {
  return builder()
          .hostname(hostname)
          .tcpPort(tcpPort)
          .connectionString(connectionString)
          .build();
 }

 public static Builder builder() {
  return new AutoValue_JoinServerConfigs.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {

  public abstract Builder connectionString(String connectionString);

  public abstract Builder hostname(String hostname);

  public abstract Builder tcpPort(Integer tcpPort);

  public abstract JoinServerConfigs build();
 }
}
