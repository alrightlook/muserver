package messages;

import com.google.auto.value.AutoValue;
import utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class ConnectionResponse extends AbstractSerializer {
 public static ConnectionResponse create(C1C3Header header, byte result) {
  return builder()
      .header(header)
      .result(result)
      .build();
 }

 public static Builder builder() {
  return new AutoValue_ConnectionResponse.Builder();
 }

 public abstract C1C3Header header();

 public abstract byte result();

 @Override
 public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
  header().serialize(stream);
  StreamUtils.writeByte(result(), stream);
  return stream.toByteArray();
 }

 @AutoValue.Builder
 public abstract static class Builder {
  public abstract Builder header(C1C3Header header);

  public abstract Builder result(byte result);

  public abstract ConnectionResponse build();
 }
}
