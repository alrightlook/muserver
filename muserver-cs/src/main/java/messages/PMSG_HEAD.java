package messages;

import com.google.auto.value.AutoValue;
import muserver.utils.EndianUtils;

import java.io.*;

@AutoValue
public abstract class PMSG_HEAD extends AbstractPacket<PMSG_HEAD> {
 public static int sizeOf() {
  return 3;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_HEAD.Builder();
 }

 public static PMSG_HEAD create(Byte type, Byte size, Byte headCode) {
  return builder()
      .type(type)
      .size(size)
      .headCode(headCode)
      .build();
 }

 public static PMSG_HEAD deserialize(ByteArrayInputStream stream) throws IOException {
  return PMSG_HEAD.create(
      EndianUtils.readByte(stream),
      EndianUtils.readByte(stream),
      EndianUtils.readByte(stream)
  );
 }

 public abstract Byte type();

 public abstract Byte size();

 public abstract Byte headCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  EndianUtils.writeByte(stream, type());
  EndianUtils.writeByte(stream, size());
  EndianUtils.writeByte(stream, headCode());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder type(Byte type);

  public abstract Builder size(Byte size);

  public abstract Builder headCode(Byte headCode);

  public abstract PMSG_HEAD build();
 }
}