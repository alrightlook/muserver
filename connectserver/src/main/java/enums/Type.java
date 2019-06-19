package enums;

public enum Type {
 C1(0xC1), C2(0xC2), C3(0xC3), C4(0xC4);

 private final int type;

 Type(int type) {
  this.type = type;
 }

 public byte type() {
  return (byte) type;
 }

 public static Type toPacketType(int type) {
  for (Type value : Type.values()) {
   if (value.type() == type) {
    return value;
   }
  }
  throw new IllegalArgumentException("No enum constant: " + type);
 }
}
