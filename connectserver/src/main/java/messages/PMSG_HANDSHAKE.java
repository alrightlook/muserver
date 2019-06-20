package messages;

import com.google.auto.value.AutoValue;
import utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_HANDSHAKE extends AbstractPacket {
 public static PMSG_HANDSHAKE create(PMSG_HEAD header, byte result) {
  return builder()
      .header(header)
      .result(result)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_PMSG_HANDSHAKE.Builder();
 }

 public abstract PMSG_HEAD header();

 public abstract byte result();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  StreamUtils.writeByte(result(), stream);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PMSG_HEAD header);

  public abstract Builder result(byte result);

  public abstract PMSG_HANDSHAKE build();
 }
}
