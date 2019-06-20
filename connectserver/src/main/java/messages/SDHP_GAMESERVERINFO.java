package messages;

import com.google.auto.value.AutoValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@AutoValue
public abstract class SDHP_GAMESERVERINFO extends AbstractPacket {
    public abstract PMSG_HEAD header();
    public abstract int itemCount();

    public static SDHP_GAMESERVERINFO create(PMSG_HEAD header, int itemCount) {
        return builder()
                .header(header)
                .itemCount(itemCount)
                .build();
    }

    @Override
    public byte[] serialize(ByteArrayOutputStream stream) throws IOException {
        return new byte[0];
    }

    public static Builder builder() {
        return new AutoValue_SDHP_GAMESERVERINFO.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder header(PMSG_HEAD header);

        public abstract Builder itemCount(int itemCount);

        public abstract SDHP_GAMESERVERINFO build();
    }
}
