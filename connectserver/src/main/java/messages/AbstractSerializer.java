package messages;

import java.io.*;

public abstract class AbstractSerializer implements Serializable {
 public abstract byte[] serialize(ByteArrayOutputStream stream) throws IOException;
}