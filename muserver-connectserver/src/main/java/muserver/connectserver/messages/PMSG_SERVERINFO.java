package muserver.connectserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;
import muserver.common.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_SERVERINFO extends AbstractPacket<PMSG_SERVERINFO> {
 public static Short sizeOf() {
  return 16;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_SERVERINFO.Builder();
 }

 public static PMSG_SERVERINFO create(PBMSG_HEAD header, Byte serverGroup, Short serverCode, Byte percent, Short userCount, Short accountCount, Short pcBangCount, Short maxUserCount) {
  return builder()
      .header(header)
      .serverGroup(serverGroup)
      .serverCode(serverCode)
      .percent(percent)
      .userCount(userCount)
      .accountCount(accountCount)
      .pcBangCount(pcBangCount)
      .maxUserCount(maxUserCount)
      .build();
 }

 public static PMSG_SERVERINFO deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);
  return PMSG_SERVERINFO.create(
      header,
      EndianUtils.readByte(stream),
      EndianUtils.readShort(stream),
      EndianUtils.readByte(stream),
      EndianUtils.readShort(stream),
      EndianUtils.readShort(stream),
      EndianUtils.readShort(stream),
      EndianUtils.readShort(stream)
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract Byte serverGroup();

 public abstract Short serverCode();

 public abstract Byte percent();

 public abstract Short userCount();

 public abstract Short accountCount();

 public abstract Short pcBangCount();

 public abstract Short maxUserCount();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeShortLE(stream, serverCode());
  EndianUtils.writeByte(stream, percent());
  EndianUtils.writeShortLE(stream, userCount());
  EndianUtils.writeShortLE(stream, accountCount());
  EndianUtils.writeShortLE(stream, pcBangCount());
  EndianUtils.writeShortLE(stream, maxUserCount());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder serverGroup(Byte serverGroup);

  public abstract Builder serverCode(Short serverCode);

  public abstract Builder percent(Byte percent);

  public abstract Builder userCount(Short userCount);

  public abstract Builder accountCount(Short accountCount);

  public abstract Builder pcBangCount(Short pcBangCount);

  public abstract Builder maxUserCount(Short maxUserCount);

  public abstract PMSG_SERVERINFO build();
 }
}
