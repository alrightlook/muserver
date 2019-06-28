package messages;

import com.google.auto.value.AutoValue;
import utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AutoValue
public abstract class PMSG_SERVERLIST extends AbstractPacket<PMSG_SERVERLIST> {
 public static Builder builder() {
  return new AutoValue_PMSG_SERVERLIST.Builder();
 }

 public static PMSG_SERVERLIST create(PWMSG_HEAD2 header, short count, List<PMSG_SERVER> serverList) {
  return builder()
      .header(header)
      .count(count)
      .serverList(serverList)
      .build();
 }

 public static PMSG_SERVERLIST deserialize(ByteArrayInputStream stream) throws IOException {
  PWMSG_HEAD2 header = PWMSG_HEAD2.deserialize(stream);

  short count = EndianUtils.readShort(stream);

  List<PMSG_SERVER> serverList = new ArrayList<>();

  for (int i = 0; i < count; i++) {
   serverList.add(PMSG_SERVER.deserialize(stream));
  }

  return PMSG_SERVERLIST.create(header, count, serverList);
 }

 public abstract PWMSG_HEAD2 header();

 public abstract short count();

 public abstract List<PMSG_SERVER> serverList();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  EndianUtils.writeShort(stream, count());
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

  public abstract PMSG_SERVERLIST build();
 }
}
