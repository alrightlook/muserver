package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;
import muserver.common.GlobalDefinitions;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
{
	BYTE Index;
	char Name[MAX_IDSTRING];
	WORD Level;
	BYTE Class;
	BYTE dbInventory[24];
} SDHP_CHARLIST, * LPSDHP_CHARLIST;
 */

@AutoValue
public abstract class SDHP_CHARLIST extends AbstractPacket<SDHP_CHARLIST> {
    public static Builder builder() {
        return new AutoValue_SDHP_CHARLIST.Builder();
    }

    public static SDHP_CHARLIST create(Byte index, String name, Short level, Byte clazz, byte[] dbInventory) {
        return builder()
                .index(index)
                .name(name)
                .level(level)
                .clazz(clazz)
                .dbInventory(dbInventory)
                .build();
    }

    public static SDHP_CHARLIST deserialize(ByteArrayInputStream stream) throws IOException {
        return SDHP_CHARLIST.create(
                EndianUtils.readByte(stream),
                new String(EndianUtils.readBytes(stream, GlobalDefinitions.MAX_IDSTRING)),
                EndianUtils.readShortLE(stream),
                EndianUtils.readByte(stream),
                EndianUtils.readBytes(stream, 24)
        );
    }

    public abstract Byte index();

    public abstract String name();

    public abstract Short level();

    public abstract Byte clazz();

    public abstract byte[] dbInventory();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        EndianUtils.writeByte(stream, index());
        EndianUtils.writeString(stream, name(), GlobalDefinitions.MAX_IDSTRING);
        EndianUtils.writeShortLE(stream, level());
        EndianUtils.writeByte(stream, clazz());
        EndianUtils.writeBytes(stream, dbInventory());
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder index(Byte index);

        public abstract Builder name(String name);

        public abstract Builder level(Short level);

        public abstract Builder clazz(Byte clazz);

        public abstract Builder dbInventory(byte[] dbInventory);

        public abstract SDHP_CHARLIST build();
    }
}
