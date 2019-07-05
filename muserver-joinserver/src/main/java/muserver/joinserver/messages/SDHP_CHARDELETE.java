package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;
import muserver.common.Globals;
import muserver.common.messages.PBMSG_HEAD;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
typedef struct
{
	PBMSG_HEAD	h;
	short		Number;
	char		AccountID[MAX_IDSTRING];
	char		Name[MAX_IDSTRING];
	BYTE		Guild;
	char		GuildName[MAX_GUILDNAMESTRING];
} SDHP_CHARDELETE, *LPSDHP_CHARDELETE;
 */
@AutoValue
public abstract class SDHP_CHARDELETE extends AbstractPacket<SDHP_CHARDELETE> {
    public static Builder builder() {
        return new AutoValue_SDHP_CHARDELETE.Builder();
    }

    public static SDHP_CHARDELETE create(PBMSG_HEAD header, Short number, String accountId, String name, Byte guild, String guildName) {
        return builder()
                .header(header)
                .number(number)
                .accountId(accountId)
                .name(name)
                .guild(guild)
                .guildName(guildName)
                .build();
    }

    public static SDHP_CHARDELETE deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_CHARDELETE.create(
                header,
                EndianUtils.readShortLE(stream),
                new String(EndianUtils.readBytes(stream, Globals.MAX_IDSTRING)),
                new String(EndianUtils.readBytes(stream, Globals.MAX_IDSTRING)),
                EndianUtils.readByte(stream),
                new String(EndianUtils.readBytes(stream, Globals.MAX_GUILDNAMESTRING))
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Short number();

    public abstract String accountId();

    public abstract String name();

    public abstract Byte guild();

    public abstract String guildName();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        EndianUtils.writeShortLE(stream, number());
        EndianUtils.writeString(stream, accountId(), Globals.MAX_IDSTRING);
        EndianUtils.writeString(stream, name(), Globals.MAX_IDSTRING);
        EndianUtils.writeByte(stream, guild());
        EndianUtils.writeString(stream, guildName(), Globals.MAX_GUILDNAMESTRING);
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder number(Short number);

        public abstract Builder accountId(String accountId);

        public abstract Builder name(String name);

        public abstract Builder guild(Byte guild);

        public abstract Builder guildName(String guildName);

        public abstract SDHP_CHARDELETE build();
    }
}
