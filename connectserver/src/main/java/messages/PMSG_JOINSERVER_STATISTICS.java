package messages;

import com.google.auto.value.AutoValue;
import utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_JOINSERVER_STATISTICS extends AbstractPacket<PMSG_JOINSERVER_STATISTICS> {
 public int sizeOf() {
  return 7;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_JOINSERVER_STATISTICS.Builder();
 }

 public static PMSG_JOINSERVER_STATISTICS create(PMSG_HEAD header, int itemCount) {
  return builder()
      .header(header)
      .queueSize(itemCount)
      .build();
 }

 public static PMSG_JOINSERVER_STATISTICS deserialize(ByteArrayInputStream stream) throws IOException {
  PMSG_HEAD header = PMSG_HEAD.deserialize(stream);
  return PMSG_JOINSERVER_STATISTICS.create(header, EndianUtils.readByte(stream));
 }

 public abstract PMSG_HEAD header();

 public abstract int queueSize();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeInteger(stream, queueSize());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PMSG_HEAD header);

  public abstract Builder queueSize(int queueSize);

  public abstract PMSG_JOINSERVER_STATISTICS build();
 }
}