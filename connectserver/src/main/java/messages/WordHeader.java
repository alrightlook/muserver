package messages;

import com.google.auto.value.AutoValue;
import enums.Type;
import utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class WordHeader extends AbstractPacket {
 public static WordHeader create(Type type, short size, byte headCode) {
  return builder()
      .type(type)
      .size(size)
      .headCode(headCode)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_WordHeader.Builder();
 }

 public abstract Type type();

 public abstract short size();

 public abstract byte headCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  StreamUtils.writeByte(type().type(), stream);
  StreamUtils.writeShort(size(), stream);
  StreamUtils.writeByte(headCode(), stream);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder type(Type type);

  public abstract Builder size(short size);

  public abstract Builder headCode(byte headCode);

  public abstract WordHeader build();
 }
}
