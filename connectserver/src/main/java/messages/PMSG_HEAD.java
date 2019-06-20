package messages;

import com.google.auto.value.AutoValue;
import utils.StreamUtils;

import java.io.*;

@AutoValue
public abstract class PMSG_HEAD extends AbstractPacket {
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

 public abstract Byte type();

 public abstract Byte size();

 public abstract Byte headCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  StreamUtils.writeByte(type(), stream);
  StreamUtils.writeByte(size(), stream);
  StreamUtils.writeByte(headCode(), stream);
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