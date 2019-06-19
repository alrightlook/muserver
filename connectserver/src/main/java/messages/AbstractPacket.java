package messages;

import java.io.*;

public abstract class AbstractPacket implements Serializable {
 public abstract byte[] serialize(ByteArrayOutputStream stream) throws IOException;
}