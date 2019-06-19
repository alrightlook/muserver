package messages;

import com.google.auto.value.AutoValue;
import enums.Type;
import utils.StreamUtils;

import java.io.*;

@AutoValue
public abstract class ByteHeader extends AbstractPacket {
 public abstract Type type();

 public abstract byte size();

 public abstract byte headCode();

 public abstract byte subCode();

 public static ByteHeader create(Type type, byte size, byte headCode) {
  return builder()
          .type(type)
          .size(size)
          .headCode(headCode)
          .build();
 }

 public static ByteHeader create(Type type, byte size, byte headCode, byte subCode) {
  return builder()
          .type(type)
          .size(size)
          .headCode(headCode)
          .subCode(subCode)
          .build();
 }

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  StreamUtils.writeByte(type().type(), stream);
  StreamUtils.writeByte(size(), stream);
  StreamUtils.writeByte(headCode(), stream);
  return stream.toByteArray();
 }

 public static Builder builder() {
  return new AutoValue_Byte_Header.Builder();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder type(Type type);

  public abstract Builder size(byte size);

  public abstract Builder headCode(byte headCode);

  public abstract Builder subCode(byte subCode);

  public abstract ByteHeader build();
 }
}