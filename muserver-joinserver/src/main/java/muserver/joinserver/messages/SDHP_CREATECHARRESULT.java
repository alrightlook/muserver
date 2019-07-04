package muserver.joinserver.messages;

/*
typedef struct
{
	PBMSG_HEAD	h;
	BYTE		Result;
	short		Number;
	char		AccountId[MAX_IDSTRING];
	char        Name[MAX_IDSTRING];
	BYTE		Pos;
	BYTE		ClassSkin;
	BYTE		Equipment[24];	// ВшїлЗП°н АЦґВ Аеєс
} SDHP_CREATECHARRESULT, *LPSDHP_CREATECHARRESULT;
 */

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;
import muserver.common.GlobalDefinitions;
import muserver.common.messages.PBMSG_HEAD;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class SDHP_CREATECHARRESULT extends AbstractPacket<SDHP_CREATECHARRESULT> {
    public static Builder builder() {
        return new AutoValue_SDHP_CREATECHARRESULT.Builder();
    }

    public static SDHP_CREATECHARRESULT create(PBMSG_HEAD header, Byte result, Short number, String accountId, String name, Byte pos, Byte classSkin, byte[] equipment) {
        return builder()
                .header(header)
                .result(result)
                .number(number)
                .accountId(accountId)
                .name(name)
                .pos(pos)
                .classSkin(classSkin)
                .equipment(equipment)
                .build();
    }

    public static SDHP_CREATECHARRESULT deserialize(ByteArrayInputStream stream) throws IOException {
        PBMSG_HEAD header = PBMSG_HEAD.deserialize(stream);

        return SDHP_CREATECHARRESULT.create(
                header,
                EndianUtils.readByte(stream),
                EndianUtils.readShortLE(stream),
                new String(EndianUtils.readBytes(stream, GlobalDefinitions.MAX_IDSTRING)),
                new String(EndianUtils.readBytes(stream, GlobalDefinitions.MAX_IDSTRING)),
                EndianUtils.readByte(stream),
                EndianUtils.readByte(stream),
                EndianUtils.readBytes(stream, 24)
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Byte result();

    public abstract Short number();

    public abstract String accountId();

    public abstract String name();

    public abstract Byte pos();

    public abstract Byte classSkin();

    public abstract byte[] equipment();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        EndianUtils.writeByte(stream, result());
        EndianUtils.writeShortLE(stream, number());
        EndianUtils.writeString(stream, accountId(), GlobalDefinitions.MAX_IDSTRING);
        EndianUtils.writeString(stream, name(), GlobalDefinitions.MAX_IDSTRING);
        EndianUtils.writeByte(stream, pos());
        EndianUtils.writeByte(stream, classSkin());
        EndianUtils.writeBytes(stream, equipment());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PBMSG_HEAD header);

        public abstract Builder result(Byte result);

        public abstract Builder number(Short number);

        public abstract Builder accountId(String accountId);

        public abstract Builder name(String name);

        public abstract Builder pos(Byte pos);

        public abstract Builder classSkin(Byte classSkin);

        public abstract Builder equipment(byte[] equipment);

        public abstract SDHP_CREATECHARRESULT build();
    }
}
