package muserver.cs.messages;

import com.google.auto.value.AutoValue;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_SERVER extends AbstractPacket<PMSG_SERVER> {
 public static int sizeOf() {
  return 4;
 }

 public static PMSG_SERVER create(Short serverCode, Byte usersCount, Byte type) {
  return builder()
      .serverCode(serverCode)
      .usersCount(usersCount)
      .type(type)
      .build();
 }

 public static PMSG_SERVER deserialize(ByteArrayInputStream stream) throws IOException {
  return PMSG_SERVER.create(
      EndianUtils.readShortLE(stream),
      EndianUtils.readByte(stream),
      EndianUtils.readByte(stream)
  );
 }

 public static Builder builder() {
  return new AutoValue_PMSG_SERVER.Builder();
 }

 public abstract Short serverCode();

 public abstract Byte usersCount();

 public abstract Byte type();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  EndianUtils.writeShortLE(stream, serverCode());
  EndianUtils.writeByte(stream, usersCount());
  EndianUtils.writeByte(stream, type());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder serverCode(Short serverCode);

  public abstract Builder usersCount(Byte usersCount);

  public abstract Builder type(Byte type);

  public abstract PMSG_SERVER build();
 }
}
