package messages;

import com.google.auto.value.AutoValue;
import utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_HANDSHAKE extends AbstractPacket<PMSG_HANDSHAKE> {
 public static int sizeOf() {
  return 4;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_HANDSHAKE.Builder();
 }

 public static PMSG_HANDSHAKE create(PMSG_HEAD header, byte result) {
  return builder()
      .header(header)
      .result(result)
      .build();
 }

 public static PMSG_HANDSHAKE deserialize(ByteArrayInputStream stream) throws IOException {
  PMSG_HEAD header = PMSG_HEAD.deserialize(stream);
  return PMSG_HANDSHAKE.create(header, EndianUtils.readByte(stream));
 }

 public abstract PMSG_HEAD header();

 public abstract byte result();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeByte(stream, result());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PMSG_HEAD header);

  public abstract Builder result(byte result);

  public abstract PMSG_HANDSHAKE build();
 }
}
