package messages;

import com.google.auto.value.AutoValue;
import utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class SDHP_GAMESERVERINFO extends AbstractPacket {
    public static SDHP_GAMESERVERINFO create(PMSG_HEAD header, int itemCount) {
        return builder()
                .header(header)
                .itemCount(itemCount)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_SDHP_GAMESERVERINFO.Builder();
    }

    public abstract PMSG_HEAD header();

    public abstract int itemCount();

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        header().serialize(stream);
        StreamUtils.writeInt(itemCount(), stream);
        return stream.toByteArray();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PMSG_HEAD header);

        public abstract Builder itemCount(int itemCount);

        public abstract SDHP_GAMESERVERINFO build();
    }
}
