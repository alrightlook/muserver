package messages;

import com.google.auto.value.AutoValue;
import utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class PMSG_SERVERINFO extends AbstractPacket {
    public abstract PMSG_HEAD header();
    public abstract short serverCode();
    public abstract byte percent();
    public abstract short userCount();
    public abstract short accountCount();
    public abstract short pcBangCount();
    public abstract short maxUserCount();

    public static PMSG_SERVERINFO create(PMSG_HEAD header, short serverCode, byte percent, short userCount, short accountCount, short pcBangCount, short maxUserCount) {
        return builder()
                .header(header)
                .serverCode(serverCode)
                .percent(percent)
                .userCount(userCount)
                .accountCount(accountCount)
                .pcBangCount(pcBangCount)
                .maxUserCount(maxUserCount)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PMSG_SERVERINFO.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PMSG_HEAD header);

        public abstract Builder serverCode(short serverCode);

        public abstract Builder percent(byte percent);

        public abstract Builder userCount(short userCount);

        public abstract Builder accountCount(short accountCount);

        public abstract Builder pcBangCount(short pcBangCount);

        public abstract Builder maxUserCount(short maxUserCount);

        public abstract PMSG_SERVERINFO build();
    }

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        StreamUtils.writeShort(serverCode(), stream);
        StreamUtils.writeByte(percent(), stream);
        StreamUtils.writeShort(userCount(), stream);
        StreamUtils.writeShort(accountCount(), stream);
        StreamUtils.writeShort(pcBangCount(), stream);
        StreamUtils.writeShort(maxUserCount(), stream);
        return stream.toByteArray();
    }
}
