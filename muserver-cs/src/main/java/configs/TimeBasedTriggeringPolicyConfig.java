package configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TimeBasedTriggeringPolicyConfig {
 @JsonCreator
 public static TimeBasedTriggeringPolicyConfig create(
     @JsonProperty("interval") Integer interval,
     @JsonProperty("modulate") Boolean modulate
 ) {
  return builder()
      .interval(interval)
      .modulate(modulate)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_TimeBasedTriggeringPolicyConfig.Builder();
 }

 @JsonProperty("interval")
 public abstract Integer interval();

 @JsonProperty("modulate")
 public abstract Boolean modulate();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder interval(Integer interval);

  public abstract Builder modulate(Boolean modulate);

  public abstract TimeBasedTriggeringPolicyConfig build();
 }
}
