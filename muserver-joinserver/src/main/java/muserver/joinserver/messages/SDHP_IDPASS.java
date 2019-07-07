package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
typedef struct
{
	PBMSG_HEAD	h;

	char		Id[MAX_IDSTRING];
	char		Pass[MAX_IDSTRING];
	short		Number;
	char		IpAddress[17];
} SDHP_IDPASS, *LPSDHP_IDPASS;
 */

import static muserver.utils.EndianUtils.*;

@AutoValue
public abstract class SDHP_IDPASS extends AbstractPacket<SDHP_IDPASS> {
 public static Builder builder() {
  return new AutoValue_SDHP_IDPASS.Builder();
 }

 public static SDHP_IDPASS create(PBMSG_HEAD header, String id, String pass, Short number, String ipAddress) {
  return builder()
      .header(header)
      .id(id)
      .pass(pass)
      .number(number)
      .ipAddress(ipAddress)
      .build();
 }

 public static SDHP_IDPASS deserialize(ByteArrayInputStream stream) throws IOException {
  PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

  return SDHP_IDPASS.create(
      header,
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      new String(readBytes(stream, Globals.MAX_IDSTRING)),
      readShortLE(stream),
      new String(readBytes(stream, Globals.MAX_IPADDRESS))
  );
 }

 public abstract PBMSG_HEAD header();

 public abstract String id();

 public abstract String pass();

 public abstract Short number();

 public abstract String ipAddress();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  writeString(stream, id(), Globals.MAX_IDSTRING);
  writeString(stream, pass(), Globals.MAX_IDSTRING);
  writeShortLE(stream, number());
  writeString(stream, ipAddress(), Globals.MAX_IPADDRESS);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD header);

  public abstract Builder id(String id);

  public abstract Builder pass(String pass);

  public abstract Builder number(Short number);

  public abstract Builder ipAddress(String ipAddress);

  public abstract SDHP_IDPASS build();
 }
}
