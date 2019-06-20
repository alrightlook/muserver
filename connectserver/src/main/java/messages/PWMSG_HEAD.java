package messages;

import com.google.auto.value.AutoValue;
import utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PWMSG_HEAD extends AbstractPacket {
 public static Builder builder() {
  return new AutoValue_PWMSG_HEAD.Builder();
 }

 public static PWMSG_HEAD create(Byte type, Short size, Byte headCode) {
  return builder()
          .type(type)
          .size(size)
          .headCode(headCode)
          .build();
 }

 public abstract Byte type();

 public abstract Short size();

 public abstract Byte headCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  StreamUtils.writeByte(type(), stream);
  StreamUtils.writeShort(size(), stream);
  StreamUtils.writeByte(headCode(), stream);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder type(Byte type);

  public abstract Builder size(Short size);

  public abstract Builder headCode(Byte headCode);

  public abstract PWMSG_HEAD build();
 }
}
