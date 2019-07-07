package muserver.common.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class JoinServerConfigs extends AbstractConfigs {
 @JsonProperty("connectServerIp")
 public abstract String connectServerIp();

 @JsonProperty("connectServerPort")
 public abstract Integer connectServerPort();

 @JsonProperty("connectionString")
 public abstract String connectionString();

 @JsonCreator
 public static JoinServerConfigs create(
     @JsonProperty("connectServerIp") String connectServerIp,
     @JsonProperty("connectServerPort") Integer connectServerPort,
     @JsonProperty("connectionString") String connectionString
 ) {
  return builder()
      .connectServerIp(connectServerIp)
      .connectServerPort(connectServerPort)
      .connectionString(connectionString)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_JoinServerConfigs.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder connectServerIp(String connectServerIp);

  public abstract Builder connectServerPort(Integer connectServerPort);

  public abstract Builder connectionString(String connectionString);

  public abstract JoinServerConfigs build();
 }
}
