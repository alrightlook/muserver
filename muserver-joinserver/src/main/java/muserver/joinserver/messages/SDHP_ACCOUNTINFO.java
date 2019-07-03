package muserver.joinserver.messages;

import com.google.auto.value.AutoValue;
import muserver.common.AbstractPacket;
import muserver.common.messages.PBMSG_HEAD;
import muserver.utils.EndianUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
                new String(EndianUtils.readBytes(stream, 11))
        );
    }

    public abstract PBMSG_HEAD header();

    public abstract Byte serverCode();

    public abstract Byte subCode();

    public abstract Integer number();

    public abstract String accountId(); //10 + 1

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        EndianUtils.writeByte(stream, serverCode());
        EndianUtils.writeByte(stream, subCode());
        EndianUtils.writeIntegerBE(stream, number());

        byte[] accountId = new byte[11];

        int i = 0;
        for (byte value : accountId().getBytes()) {
            accountId[i] = value;
            i++;
        }

        EndianUtils.writeBytes(stream, accountId);

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
