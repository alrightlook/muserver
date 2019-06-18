package messages;

import com.google.auto.value.AutoValue;
import enums.PacketType;
import utils.StreamUtils;

import java.io.*;

@AutoValue
public abstract class C1C3Header extends AbstractSerializer {
 public static C1C3Header create(PacketType type, byte size, byte headCode) {
  return builder()
      .type(type)
      .size(size)
      .headCode(headCode)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_C1C3_Header.Builder();
 }

 public abstract PacketType type();

 public abstract byte size();

 public abstract byte headCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  StreamUtils.writeByte(type().type(), stream);
  StreamUtils.writeByte(size(), stream);
  StreamUtils.writeByte(headCode(), stream);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder type(PacketType type);

  public abstract Builder size(byte size);

  public abstract Builder headCode(byte headCode);

  public abstract C1C3Header build();
 }
}