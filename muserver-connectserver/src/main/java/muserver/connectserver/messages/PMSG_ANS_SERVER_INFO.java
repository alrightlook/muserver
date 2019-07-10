package muserver.connectserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD2;
import muserver.common.utils.EndianUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_ANS_SERVER_INFO extends AbstractPacket<PMSG_ANS_SERVER_INFO> {
 public static int sizeOf() {
  return PBMSG_HEAD2.sizeOf() + 16 + 2;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_ANSSERVERINFO.Builder();
 }

 public static PMSG_ANS_SERVER_INFO create(PBMSG_HEAD2 header, String serverAddress, Short serverPort) {
  return builder()
      .header(header)
      .serverAddress(serverAddress)
      .serverPort(serverPort)
      .build();
 }

 public abstract PBMSG_HEAD2 header();

 public abstract String serverAddress();

 public abstract Short serverPort();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeString(stream, serverAddress(), 16);
  EndianUtils.writeShortBE(stream, serverPort());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PBMSG_HEAD2 header);

  public abstract Builder serverAddress(String ipAddress);

  public abstract Builder serverPort(Short portNumber);

  public abstract PMSG_ANS_SERVER_INFO build();
 }
}
