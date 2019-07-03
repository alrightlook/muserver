package muserver.cs.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import muserver.cs.enums.LoggingLevel;

import java.util.List;

@AutoValue
public abstract class LoggingConfigs {
 @JsonCreator
 public static LoggingConfigs create(
     @JsonProperty("level") LoggingLevel level,
     @JsonProperty("appenders") List<AppenderConfigs> appenders
 ) {
  return builder()
      .level(level)
      .appenders(appenders)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_LoggingConfigs.Builder();
 }

 @JsonProperty("level")
 public abstract LoggingLevel level();

 @JsonProperty("appenders")
 public abstract List<AppenderConfigs> appenders();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder level(LoggingLevel level);

  public abstract Builder appenders(List<AppenderConfigs> appenders);

  public abstract LoggingConfigs build();
 }
}
