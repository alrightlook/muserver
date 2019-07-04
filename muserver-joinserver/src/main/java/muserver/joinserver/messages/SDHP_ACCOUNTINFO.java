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
	BYTE		ServerCode;
	BYTE		Subcode;

	int			Number;
	char		AccountID[MAX_IDSTRING+1];// °иБ¤ ѕЖАМµр

} SDHP_ACCOUNTINFO, * LPSDHP_ACCOUNTINFO;
 */
@AutoValue
public abstract class SDHP_ACCOUNTINFO extends AbstractPacket<SDHP_ACCOUNTINFO> {
    public int sizeOf() { return 20; }

    public static Builder builder() {
        return new AutoValue_SDHP_ACCOUNTINFO.Builder();
    }

    public static SDHP_ACCOUNTINFO create(PBMSG_HEAD header, Byte serverCode, Byte subCode, Integer number, String accountId) {
        return builder()
                .header(header)
                .serverCode(serverCode)
                .subCode(subCode)
                .number(number)
                .accountId(accountId)
                .build();
    }

    public static SDHP_ACCOUNTINFO deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_ACCOUNTINFO.create(
                header,
                EndianUtils.readByte(stream),
                EndianUtils.readByte(stream),
                EndianUtils.readIntegerLE(stream),
                new String(EndianUtils.readBytes(stream, GlobalDefinitions.MAX_IDSTRING + 1))
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Byte serverCode();

    public abstract Byte subCode();

    public abstract Integer number();

    public abstract String accountId();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        EndianUtils.writeByte(stream, serverCode());
        EndianUtils.writeByte(stream, subCode());
        EndianUtils.writeIntegerBE(stream, number());
        EndianUtils.writeString(stream, accountId(), GlobalDefinitions.MAX_IDSTRING);
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder serverCode(Byte serverCode);

        public abstract Builder subCode(Byte subCode);

        public abstract Builder number(Integer number);

        public abstract Builder accountId(String accountId);

        public abstract SDHP_ACCOUNTINFO build();
    }
}
