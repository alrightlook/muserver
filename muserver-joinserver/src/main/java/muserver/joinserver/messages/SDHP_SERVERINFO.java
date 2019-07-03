package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public class SDHP_SERVERINFO extends AbstractPacket<SDHP_SERVERINFO> {
    @Override
    public byte[] serialize(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        return new byte[0];
    }
}
