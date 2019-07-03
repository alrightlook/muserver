package muserver.connectserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD2;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_SERVER_CODE extends AbstractPacket<PMSG_SERVER_CODE> {
 public static int sizeOf() {
  return 5;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_SERVER_CODE.Builder();
 }

 public static PMSG_SERVER_CODE create(PBMSG_HEAD2 header, Byte serverCode) {
  return builder()
      .header(header)
      .serverCode(serverCode)
      .build();
 }

 public static PMSG_SERVER_CODE deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD2 header = PBMSG_HEAD2.deserialize(stream);
  return PMSG_SERVER_CODE.create(header, EndianUtils.readByte(stream));
 }

 public abstract PBMSG_HEAD2 header();

 public abstract Byte serverCode();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeByte(stream, serverCode());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD2 header);

  public abstract Builder serverCode(Byte serverCode);

  public abstract PMSG_SERVER_CODE build();
 }
}
