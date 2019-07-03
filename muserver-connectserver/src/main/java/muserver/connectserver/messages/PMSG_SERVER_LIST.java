package muserver.connectserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;
import muserver.common.messages.PWMSG_HEAD2;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class PMSG_SERVER_LIST extends AbstractPacket<PMSG_SERVER_LIST> {
 public static Builder builder() {
  return new AutoValue_PMSG_SERVER_LIST.Builder();
 }

 public static PMSG_SERVER_LIST create(PWMSG_HEAD2 header, short count, List<PMSG_SERVER> serverList) {
  return builder()
      .header(header)
      .count(count)
      .serverList(serverList)
      .build();
 }

 public static PMSG_SERVER_LIST deserialize(ByteArrayInputStream stream) throws IOException {
  PWMSG_HEAD2 header = PWMSG_HEAD2.deserialize(stream);

  short count = EndianUtils.readShortLE(stream);

  List<PMSG_SERVER> serverList = new ArrayList<>();

  for (int i = 0; i < count; i++) {
   serverList.add(PMSG_SERVER.deserialize(stream));
  }

  return PMSG_SERVER_LIST.create(header, count, serverList);
 }

 public abstract PWMSG_HEAD2 header();

 public abstract short count();

 public abstract List<PMSG_SERVER> serverList();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeShortLE(stream, count());
  for (PMSG_SERVER server : serverList()) {
   server.serialize(stream);
  }
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(PWMSG_HEAD2 header);

  public abstract Builder count(short count);

  public abstract Builder serverList(List<PMSG_SERVER> serverList);

  public abstract PMSG_SERVER_LIST build();
 }
}
