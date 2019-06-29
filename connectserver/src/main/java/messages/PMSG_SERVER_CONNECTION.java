package messages;

import com.google.auto.value.AutoValue;
import org.apache.commons.io.IOUtils;
import utils.EndianUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_SERVER_CONNECTION extends AbstractPacket<PMSG_SERVER_CONNECTION> {
 public static int sizeOf() {
  return 22;
 }

 public static Builder builder() {
  return new AutoValue_PMSG_SERVER_CONNECTION.Builder();
 }

 public static PMSG_SERVER_CONNECTION create(PMSG_HEAD2 header, String serverAddress, Short serverPort) {
  return builder()
      .header(header)
      .serverAddress(serverAddress)
      .serverPort(serverPort)
      .build();
 }

 public abstract PMSG_HEAD2 header();

 public abstract String serverAddress();

 public abstract Short serverPort();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);

  char[] serverAddress = new char[16];

  serverAddress().getChars(0, serverAddress().length(), serverAddress, 0);

  serverAddress[serverAddress().length()] = '\0';

  EndianUtils.writeByte(stream, (byte) 1) ;
  EndianUtils.writeShort(stream, serverPort());
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PMSG_HEAD2 header);

  public abstract Builder serverAddress(String ipAddress);

  public abstract Builder serverPort(Short portNumber);

  public abstract PMSG_SERVER_CONNECTION build();
 }
}
