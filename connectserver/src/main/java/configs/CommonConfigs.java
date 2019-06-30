package configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class CommonConfigs extends AbstractConfigs {
 @JsonProperty("connectServer")
 public abstract ConnectServerConfigs connectServerConfigs();

 @JsonCreator
 public static CommonConfigs create(@JsonProperty("connectServer") ConnectServerConfigs connectServerConfigs) {
  return builder()
      .connectServerConfigs(connectServerConfigs)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_CommonConfigs.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder connectServerConfigs(ConnectServerConfigs connectServerConfigs);

  public abstract CommonConfigs build();
 }
}
