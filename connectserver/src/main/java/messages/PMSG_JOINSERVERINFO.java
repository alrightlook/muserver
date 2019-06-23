package messages;

import com.google.auto.value.AutoValue;
import utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_JOINSERVERINFO extends AbstractPacket<PMSG_JOINSERVERINFO> {
 public static PMSG_JOINSERVERINFO create(PMSG_HEAD header, int itemCount) {
  return builder()
      .header(header)
      .itemCount(itemCount)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_PMSG_JOINSERVERINFO.Builder();
 }

 public abstract PMSG_HEAD header();

 public abstract int itemCount();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeInteger(stream, itemCount());
  return stream.toByteArray();
 }

 public static PMSG_JOINSERVERINFO deserialize(ByteArrayInputStream stream) throws IOException {
  PMSG_HEAD header = PMSG_HEAD.deserialize(stream);
  return PMSG_JOINSERVERINFO.create(header, EndianUtils.readByte(stream));
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PMSG_HEAD header);

  public abstract Builder itemCount(int itemCount);

  public abstract PMSG_JOINSERVERINFO build();
 }
}