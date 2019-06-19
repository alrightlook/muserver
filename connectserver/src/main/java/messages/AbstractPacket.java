package messages;

import java.io.*;

public abstract class AbstractPacket implements Serializable {
 public abstract void serialize(ByteArrayOutputStream stream) throws IOException;
}