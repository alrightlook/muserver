package muserver.cs.messages;

import com.google.auto.value.AutoValue;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_JOINSERVER_INFO extends AbstractPacket<PMSG_JOINSERVER_INFO> {
 public int sizeOf() {
  return 7;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_JOINSERVER_INFO.Builder();
 }

 public static PMSG_JOINSERVER_INFO create(PMSG_HEAD header, Integer queueSize) {
  return builder()
      .header(header)
      .queueSize(queueSize)
      .build();
 }

 public static PMSG_JOINSERVER_INFO deserialize(ByteArrayInputStream stream) throws IOException {
  PMSG_HEAD header = PMSG_HEAD.deserialize(stream);
  return PMSG_JOINSERVER_INFO.create(header, EndianUtils.readIntegerLE(stream));
 }

 public abstract PMSG_HEAD header();

 public abstract Integer queueSize();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeIntegerLE(stream, queueSize());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PMSG_HEAD header);

  public abstract Builder queueSize(Integer queueSize);

  public abstract PMSG_JOINSERVER_INFO build();
 }
}