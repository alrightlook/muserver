package enums;

public enum PacketType {
 C1(0xC1), C2(0xC2), C3(0xC3), C4(0xC4);

 private final int type;

 PacketType(int type) {
  this.type = type;
 }

 public byte type() {
  return (byte) type;
 }

 public static PacketType toPacketType(int type) {
  for (PacketType value : PacketType.values()) {
   if (value.type() == type) {
    return value;
   }
  }
  throw new IllegalArgumentException("No enum constant: " + type);
 }
}
