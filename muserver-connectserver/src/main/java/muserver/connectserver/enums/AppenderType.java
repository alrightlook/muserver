package muserver.connectserver.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AppenderType {
 FILE("File"), CONSOLE("Console"), ROLLING_FILE("RollingFile");

 @JsonProperty
 private final String name;

 AppenderType(String name) {
  this.name = name;
 }

 public String getName() {
  return name;
 }

 public static AppenderType fromString(String name) {
  for (AppenderType value : AppenderType.values()) {
   if (value.getName().toUpperCase().equals(name.toUpperCase())) {
    return value;
   }
  }
  throw new IllegalArgumentException("No enum constant " + name);
 }
}
