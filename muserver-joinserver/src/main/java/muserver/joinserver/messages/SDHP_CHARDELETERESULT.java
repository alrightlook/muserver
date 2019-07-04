package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;
import muserver.common.GlobalDefinitions;
import muserver.common.messages.PBMSG_HEAD;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
typedef struct
{
	PBMSG_HEAD	h;
	BYTE		Result;
	short		Number;
	char		AccountID[MAX_IDSTRING];

} SDHP_CHARDELETERESULT, *LPSDHP_CHARDELETERESULT;
 */
@AutoValue
public abstract class SDHP_CHARDELETERESULT extends AbstractPacket<SDHP_CHARDELETERESULT> {
    public static Builder builder() {
        return new AutoValue_SDHP_CHARDELETERESULT.Builder();
    }

    public static SDHP_CHARDELETERESULT create(PBMSG_HEAD header, Byte result, Short number, String accountId) {
        return builder()
                .header(header)
                .result(result)
                .number(number)
                .accountId(accountId)
                .build();
    }

    public static SDHP_CHARDELETERESULT deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_CHARDELETERESULT.create(
                header,
                EndianUtils.readByte(stream),
                EndianUtils.readShortLE(stream),
                new String(EndianUtils.readBytes(stream, GlobalDefinitions.MAX_IDSTRING))
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Byte result();

    public abstract Short number();

    public abstract String accountId();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        EndianUtils.writeByte(stream, result());
        EndianUtils.writeShortLE(stream, number());
        EndianUtils.writeString(stream, accountId(), GlobalDefinitions.MAX_IDSTRING);
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder result(Byte result);

        public abstract Builder number(Short number);

        public abstract Builder accountId(String accountId);

        public abstract SDHP_CHARDELETERESULT build();
    }
}
