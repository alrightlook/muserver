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

	char		Id[MAX_IDSTRING];
	short		Number;

} SDHP_BILLKILLUSER, *LPSDHP_BILLKILLUSER;
 */
@AutoValue
public abstract class SDHP_BILLKILLUSER extends AbstractPacket<SDHP_BILLKILLUSER> {
    public static Builder builder() {
        return new AutoValue_SDHP_BILLKILLUSER.Builder();
    }

    public static SDHP_BILLKILLUSER create(PBMSG_HEAD header, String id, Short number) {
        return builder()
                .header(header)
                .id(id)
                .number(number)
                .build();
    }

    public static SDHP_BILLKILLUSER deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);
        return SDHP_BILLKILLUSER.create(header, new String(EndianUtils.readBytes(stream, GlobalDefinitions.MAX_IDSTRING)), EndianUtils.readShortLE(stream));
    }

    public abstract PBMSG_HEAD header();

    public abstract String id();

    public abstract Short number();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        EndianUtils.writeString(stream, id(), GlobalDefinitions.MAX_IDSTRING);
        EndianUtils.writeShortLE(stream, number());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder id(String id);

        public abstract Builder number(Short number);

        public abstract SDHP_BILLKILLUSER build();
    }
}
