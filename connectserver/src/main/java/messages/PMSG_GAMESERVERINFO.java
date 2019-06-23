package messages;

import com.google.auto.value.AutoValue;
import utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_GAMESERVERINFO extends AbstractPacket<PMSG_GAMESERVERINFO> {
 public static PMSG_GAMESERVERINFO create(PMSG_HEAD header, short serverCode, byte percent, short userCount, short accountCount, short pcBangCount, short maxUserCount) {
  return builder()
      .header(header)
      .serverCode(serverCode)
      .percent(percent)
      .userCount(userCount)
      .accountCount(accountCount)
      .pcBangCount(pcBangCount)
      .maxUserCount(maxUserCount)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_PMSG_GAMESERVERINFO.Builder();
 }

 public static PMSG_GAMESERVERINFO deserialize(ByteArrayInputStream stream) throws IOException {
  PMSG_HEAD header = PMSG_HEAD.deserialize(stream);
  return PMSG_GAMESERVERINFO.create(
      header,
      EndianUtils.readShort(stream),
      EndianUtils.readByte(stream),
      EndianUtils.readShort(stream),
      EndianUtils.readShort(stream),
      EndianUtils.readShort(stream),
      EndianUtils.readShort(stream)
  );
 }

 public abstract PMSG_HEAD header();

 public abstract short serverCode();

 public abstract byte percent();

 public abstract short userCount();

 public abstract short accountCount();

 public abstract short pcBangCount();

 public abstract short maxUserCount();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeShort(stream, serverCode());
  EndianUtils.writeByte(stream, percent());
  EndianUtils.writeShort(stream, userCount());
  EndianUtils.writeShort(stream, accountCount());
  EndianUtils.writeShort(stream, pcBangCount());
  EndianUtils.writeShort(stream, maxUserCount());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PMSG_HEAD header);

  public abstract Builder serverCode(short serverCode);

  public abstract Builder percent(byte percent);

  public abstract Builder userCount(short userCount);

  public abstract Builder accountCount(short accountCount);

  public abstract Builder pcBangCount(short pcBangCount);

  public abstract Builder maxUserCount(short maxUserCount);

  public abstract PMSG_GAMESERVERINFO build();
 }
}
