package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.messages.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static muserver.utils.EndianUtils.*;

/*
typedef struct
{
	PBMSG_HEAD	h;

	short		Number;
	char		Id[MAX_IDSTRING+1];
	int			UserNumber;
	int			DBNumber;
	BYTE		BlockCode;
} SDHP_COMMAND_BLOCK, * LPSDHP_COMMAND_BLOCK;
 */
@AutoValue
public abstract class SDHP_COMMAND_BLOCK extends AbstractPacket<SDHP_COMMAND_BLOCK> {
    public static Builder builder() {
        return new AutoValue_SDHP_COMMAND_BLOCK.Builder();
    }

    public static SDHP_COMMAND_BLOCK create(PBMSG_HEAD header, Short number, String id, Integer userNumber, Integer dbNumber, Byte blockCode) {
        return builder()
                .header(header)
                .number(number)
                .id(id)
                .userNumber(userNumber)
                .dbNumber(dbNumber)
                .blockCode(blockCode)
                .build();
    }

    public static SDHP_COMMAND_BLOCK deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_COMMAND_BLOCK.create(
                header,
                readShortLE(stream),
                new String(readBytes(stream, Globals.MAX_IDSTRING + 1)),
                readIntegerLE(stream),
                readIntegerLE(stream),
                readByte(stream)
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Short number();

    public abstract String id();

    public abstract Integer userNumber();

    public abstract Integer dbNumber();

    public abstract Byte blockCode();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        writeShortLE(stream, number());
        writeString(stream, id(), Globals.MAX_IDSTRING + 1);
        writeIntegerLE(stream, userNumber());
        writeIntegerLE(stream, dbNumber());
        writeByte(stream, blockCode());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder number(Short number);

        public abstract Builder id(String id);

        public abstract Builder userNumber(Integer userNumber);

        public abstract Builder dbNumber(Integer dbNumber);

        public abstract Builder blockCode(Byte blockCode);

        public abstract SDHP_COMMAND_BLOCK build();
    }
}
