package muserver.joinserver.messages;

/*
typedef struct
{
	PWMSG_HEAD	h;

	char  Name[MAX_IDSTRING];	// АМё§
	BYTE  dbInventory[MAX_DBINVENTORY];	// ѕЖАМЕЫ АОєҐЕдё®
} SDHP_DBCHAR_ITEMSAVE, *LPSDHP_DBCHAR_ITEMSAVE;
 */

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PWMSG_HEAD;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class SDHP_DBCHAR_ITEMSAVE extends AbstractPacket<SDHP_DBCHAR_ITEMSAVE> {
 public static Builder builder() {
  return new AutoValue_SDHP_DBCHAR_ITEMSAVE.Builder();
 }

 public static SDHP_DBCHAR_ITEMSAVE create(PWMSG_HEAD header, String name, byte[] dbInventory) {
  return builder()
      .header(header)
      .name(name)
      .dbInventory(dbInventory)
      .build();
 }

 public static SDHP_DBCHAR_ITEMSAVE deserialize(ByteArrayInputStream stream) throws IOException {
  PWMSG_HEAD header = PWMSG_HEAD.deserialize(stream);

  return SDHP_DBCHAR_ITEMSAVE.create(
      header,
      new String(EndianUtils.readBytes(stream, Globals.MAX_DBINVENTORY)),
      EndianUtils.readBytes(stream, Globals.MAX_DBINVENTORY)
  );
 }

 public abstract PWMSG_HEAD header();

 public abstract String name();

 public abstract byte[] dbInventory();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeString(stream, name(), Globals.MAX_DBINVENTORY);
  EndianUtils.writeBytes(stream, dbInventory());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PWMSG_HEAD header);

  public abstract Builder name(String name);

  public abstract Builder dbInventory(byte[] dbInventory);

  public abstract SDHP_DBCHAR_ITEMSAVE build();
 }
}
