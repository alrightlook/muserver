package muserver.common.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DataServerConfigs extends AbstractConfigs {

 public static Builder builder() {
  return new AutoValue_DataServerConfigs.Builder();
 }

 @JsonCreator
 public static DataServerConfigs create(
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

 @JsonProperty("hostname")
 public abstract String hostname();

 @JsonProperty("tcpPort")
 public abstract Integer tcpPort();

 @JsonProperty("connectionString")
 public abstract String connectionString();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder hostname(String hostname);

  public abstract Builder tcpPort(Integer tcpPort);

  public abstract Builder connectionString(String connectionString);

  public abstract DataServerConfigs build();
 }
}