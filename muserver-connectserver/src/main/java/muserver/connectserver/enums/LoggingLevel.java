package muserver.connectserver.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum  LoggingLevel {
 OFF("OFF"), FATAL("FATAL"), ERROR("ERROR"), WARN("WARN"), INFO("INFO"), DEBUG("DEBUG"), TRACE("TRACE"), ALL("ALL");

 @JsonProperty
 private final String loggingLevel;

 LoggingLevel(String loggingLevel) {
  this.loggingLevel = loggingLevel;
 }

 public String loggingLevel() {
  return loggingLevel;
 }

 public static LoggingLevel fromString(String loggingLevel) {
  for (LoggingLevel value : LoggingLevel.values()) {
   if (value.loggingLevel().toUpperCase().equals(loggingLevel.toUpperCase())) {
    return value;
   }
  }
  throw new IllegalArgumentException("No enum constant " + loggingLevel);
 }
}
