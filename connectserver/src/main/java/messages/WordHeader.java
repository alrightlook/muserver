package messages;

import com.google.auto.value.AutoValue;
import utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class WordHeader extends AbstractPacket {
 public static Builder builder() {
  return new AutoValue_WordHeader.Builder();
 }

 public abstract Byte type();

 public abstract Short size();

 public abstract Byte headCode();

 public abstract Byte subCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  StreamUtils.writeByte(type(), stream);
  StreamUtils.writeShort(size(), stream);
  StreamUtils.writeByte(headCode(), stream);

  if (subCode() != null) {
   StreamUtils.writeByte(subCode(), stream);
  }

  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder type(Byte type);

  public abstract Builder size(Short size);

  public abstract Builder headCode(Byte headCode);

  public abstract Builder subCode(Byte subCode);

  public abstract WordHeader build();
 }
}
