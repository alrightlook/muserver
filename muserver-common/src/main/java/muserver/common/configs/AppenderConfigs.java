package muserver.common.configs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import muserver.common.types.AppenderType;

import javax.annotation.Nullable;

@AutoValue
public abstract class AppenderConfigs {
 public static Builder builder() {
  return new AutoValue_AppenderConfigs.Builder();
 }

 @JsonCreator
 public static AppenderConfigs create(
     @JsonProperty("name") String name,
     @JsonProperty("type") AppenderType type,
     @JsonProperty("format") String format,
     @JsonProperty("fileName") String fileName,
     @JsonProperty("filePattern") String filePattern,
     @JsonProperty("sizeBasedTriggeringPolicy") SizeBasedTriggeringPolicyConfigs sizeBasedTriggeringPolicy
 ) {
  return builder()
      .name(name)
      .type(type)
      .format(format)
      .fileName(fileName)
      .filePattern(filePattern)
      .sizeBasedTriggeringPolicy(sizeBasedTriggeringPolicy)
      .build();
 }

 @JsonProperty("name")
 public abstract String name();

 @JsonProperty("type")
 public abstract AppenderType type();

 @JsonProperty("format")
 public abstract String format();

 @Nullable
 @JsonProperty("fileName")
 public abstract String fileName();

 @Nullable
 @JsonProperty("filePattern")
 public abstract String filePattern();

 @Nullable
 @JsonProperty("sizeBasedTriggeringPolicy")
 public abstract SizeBasedTriggeringPolicyConfigs sizeBasedTriggeringPolicy();

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder name(String name);

  public abstract Builder type(AppenderType type);

  public abstract Builder format(String format);

  public abstract Builder fileName(String fileName);

  public abstract Builder filePattern(String filePattern);

  public abstract Builder sizeBasedTriggeringPolicy(SizeBasedTriggeringPolicyConfigs sizeBasedTriggeringPolicy);

  public abstract AppenderConfigs build();
 }
}
