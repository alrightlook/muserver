package muserver.common.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ServerType {
 VISIBLE("VISIBLE"), HIDDEN("HIDDEN"),
 ;

 @JsonProperty
 private final String type;

 ServerType(String type) {
  this.type = type;
 }

 public String type() {
  return type;
 }

 public static ServerType fromString(String type) {
  for (ServerType value : ServerType.values()) {
   if (value.type().toUpperCase().equals(type.toUpperCase())) {
    return value;
   }
  }
  throw new IllegalArgumentException("No enum constant " + type);
 }
}