package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
typedef struct
{
	PBMSG_HEAD	h;

	BYTE			Type;		// 0:Join Server, 1:MapServer 2: ManagerServer
	unsigned short	Port;		// Server Port Number
	char			ServerName[50];
} SDHP_SERVERINFO, * LPSDHP_SERVERINFO;
 */
@AutoValue
public class SDHP_SERVERINFO extends AbstractPacket<SDHP_SERVERINFO> {
    @Override
    public byte[] serialize(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        return new byte[0];
    }
}
