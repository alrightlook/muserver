package configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;

@AutoValue
public abstract class SizeBasedTriggeringPolicyConfigs {
 @JsonCreator
 public static SizeBasedTriggeringPolicyConfigs create(@JsonProperty("size") String size) {
  return builder()
      .size(size)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_SizeBasedTriggeringPolicyConfigs.Builder();
 }

 @JsonProperty("size")
 public abstract String size();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder size(String size);

  public abstract SizeBasedTriggeringPolicyConfigs build();
 }
}
